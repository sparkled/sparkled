package io.sparkled.api

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.sparkled.model.animation.SequenceChannelEffects
import io.sparkled.model.enumeration.SequenceStatus
import io.sparkled.model.entity.v2.SequenceChannelEntity
import io.sparkled.model.entity.v2.SequenceEntity
import io.sparkled.model.entity.v2.SongEntity
import io.sparkled.model.entity.v2.StageEntity
import io.sparkled.model.render.RenderResult
import io.sparkled.model.render.RenderedSequence
import io.sparkled.model.render.RenderedSequenceStageProp
import io.sparkled.model.util.SequenceUtils
import io.sparkled.model.validator.exception.EntityNotFoundException
import io.sparkled.persistence.*
import io.sparkled.persistence.cache.CacheService
import io.sparkled.persistence.query.sequence.DeleteSequencesQuery
import io.sparkled.persistence.query.sequence.GetSequenceChannelsBySequenceIdQuery
import io.sparkled.persistence.query.sequence.GetSongBySequenceIdQuery
import io.sparkled.persistence.query.sequence.GetStageBySequenceIdQuery
import io.sparkled.persistence.query.stage.GetStagePropsByStageIdQuery
import io.sparkled.renderer.Renderer
import io.sparkled.renderer.SparkledPluginManager
import io.sparkled.api.response.IdResponse
import io.sparkled.viewmodel.SequenceSummaryViewModel
import io.sparkled.viewmodel.SequenceViewModel
import io.sparkled.viewmodel.StageViewModel
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.math.min

@ExecuteOn(TaskExecutors.IO)
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/sequences")
class SequenceController(
    private val pluginManager: SparkledPluginManager,
    private val objectMapper: ObjectMapper,
    private val file: FileService,
    private val caches: CacheService,
    private val db: DbService,
) {

    @Get("/")
    @Transactional(readOnly = true)
    fun getAllSequences(): HttpResponse<Any> {
        val songs = db.getAll<SongEntity>().associateBy { it.id }
        val stages = db.getAll<StageEntity>().associateBy { it.id }

        val viewModels = db.getAll<SequenceEntity>(orderBy = "name").map {
            SequenceSummaryViewModel.fromModel(it, songs.getValue(it.songId), stages.getValue(it.stageId))
        }

        return HttpResponse.ok(viewModels)
    }

    @Get("/{id}")
    @Transactional(readOnly = true)
    fun getSequence(id: Int): HttpResponse<Any> {
        val viewModel = db.getById<SequenceEntity>(id)?.let {
            val song = db.query(GetSongBySequenceIdQuery(it.id))!!
            val channels = db.query(GetSequenceChannelsBySequenceIdQuery(it.id))
            SequenceViewModel.fromModel(it, song, channels, objectMapper)
        }

        return if (viewModel != null) {
            HttpResponse.ok(viewModel)
        } else {
            HttpResponse.notFound("Sequence not found.")
        }
    }

    @Get("/{id}/stage")
    @Transactional(readOnly = true)
    fun getSequenceStage(id: Int): HttpResponse<Any> {
        val viewModel = db.query(GetStageBySequenceIdQuery(id))?.let {
            val stageProps = db.query(GetStagePropsByStageIdQuery(it.id))
            StageViewModel.fromModel(it, stageProps, objectMapper)
        }

        return if (viewModel != null) {
            HttpResponse.ok(viewModel)
        } else {
            HttpResponse.notFound("Stage not found for sequence.")
        }
    }

    @Get("/{id}/songAudio")
    @Transactional(readOnly = true)
    fun getSequenceSongAudio(id: Int): MutableHttpResponse<out Any> {
        val songAudio = db.query(GetSongBySequenceIdQuery(id))?.let { file.readSongAudio(it.id) }
        return if (songAudio != null) {
            HttpResponse.ok(songAudio).contentType("audio/mpeg")
        } else HttpResponse.notFound("Song audio not found.")
    }

    @Post("/")
    @Transactional
    fun createSequence(sequenceViewModel: SequenceViewModel): HttpResponse<Any> {

        val (sequence) = sequenceViewModel.copy(status = SequenceStatus.NEW).toModel(objectMapper)
        val savedId = db.insert(sequence).toInt()

        val sequenceChannels = createSequenceChannels(sequence.copy(id = savedId))
        sequenceChannels.map { it.copy(sequenceId = savedId) }.forEach {
            db.insert(it)
        }

        return HttpResponse.ok(IdResponse(savedId))
    }

    private fun createSequenceChannels(sequence: SequenceEntity): List<SequenceChannelEntity> {
        val stageProps = db.query(GetStagePropsByStageIdQuery(sequence.stageId))
        return stageProps.mapIndexed { i, it ->
            SequenceChannelEntity(
                uuid = UUID.randomUUID(),
                sequenceId = sequence.id,
                stagePropUuid = it.uuid,
                name = it.name,
                displayOrder = i,
                channelJson = SequenceChannelEffects.EMPTY_JSON
            )
        }
    }

    @Put("/{id}")
    @Transactional
    fun updateSequence(id: Int, sequenceViewModel: SequenceViewModel): HttpResponse<Any> {
        val sequenceAndChannels = sequenceViewModel.copy(id = id).toModel(objectMapper)
        val sequence = sequenceAndChannels.first.copy(id = id)
        val sequenceChannels = sequenceAndChannels.second.map { it.copy(sequenceId = id) }

        val newChannels = sequenceChannels.associateBy { it.uuid }
        val existingChannels = db.query(GetSequenceChannelsBySequenceIdQuery(id)).associateBy { it.uuid }

        // Delete channels that no longer exist.
        (existingChannels.keys - newChannels.keys).forEach { db.delete(existingChannels.getValue(it)) }

        // Insert channels that didn't exist previously.
        (newChannels.keys - existingChannels.keys).forEach { db.insert(newChannels.getValue(it)) }

        // Update channels that exist
        (newChannels.keys.intersect(existingChannels.keys)).forEach { db.update(newChannels.getValue(it)) }

        if (sequence.status === SequenceStatus.PUBLISHED) {
            db.update(sequence)
            val stage = db.query(GetStageBySequenceIdQuery(id)) ?: throw EntityNotFoundException("Stage not found.")
            val song = db.query(GetSongBySequenceIdQuery(id)) ?: throw EntityNotFoundException("Song not found.")
            val renderResult = renderSequence(stage, sequence, sequenceChannels, song, preview = false)
            val renderedSequence = RenderedSequence(
                sequenceId = sequence.id,
                startFrame = renderResult.startFrame,
                frameCount = renderResult.frameCount,
                stageProps = renderResult.stageProps.mapValues {
                    RenderedSequenceStageProp(
                        ledCount = it.value.ledCount,
                        base64Data = Base64.getEncoder().encodeToString(it.value.data)
                    )
                }
            )
            file.writeRender(sequence.id, renderedSequence)
        } else {
            db.update(sequence.copy(status = SequenceStatus.DRAFT))
        }

        return HttpResponse.ok()
    }

    private fun renderSequence(
        stage: StageEntity,
        sequence: SequenceEntity,
        sequenceChannels: List<SequenceChannelEntity>,
        song: SongEntity,
        startFrame: Int = 0,
        endFrame: Int = Int.MAX_VALUE,
        preview: Boolean,
    ): RenderResult {
        val stageProps = db.query(GetStagePropsByStageIdQuery(sequence.stageId))
        val endFrameBounded = min(endFrame, SequenceUtils.getFrameCount(song, sequence) - 1)

        return Renderer(
            pluginManager,
            caches.gifs.get(),
            objectMapper,
            stage,
            sequence,
            sequenceChannels,
            stageProps,
            startFrame,
            endFrameBounded,
            preview,
        ).render()
    }

    @Delete("/{id}")
    @Transactional
    fun deleteSequence(id: Int): HttpResponse<Any> {
        db.query(DeleteSequencesQuery(listOf(id)))
        return HttpResponse.ok()
    }

    @Post("/{id}/preview{?startFrame,frameCount}")
    @Transactional
    fun previewSequence(
        request: HttpRequest<Any>,
        id: Int,
        @QueryValue(defaultValue = "0") startFrame: String,
        @QueryValue(defaultValue = "0") frameCount: String,
        sequenceViewModel: SequenceViewModel
    ): HttpResponse<Any> {
        val (sequence, sequenceChannels) = sequenceViewModel.toModel(objectMapper)
        val stage = db.query(GetStageBySequenceIdQuery(id)) ?: throw EntityNotFoundException("Stage not found.")
        val song = db.query(GetSongBySequenceIdQuery(id)) ?: throw EntityNotFoundException("Song not found.")

        val start = startFrame.toInt()
        val frames = frameCount.toInt()

        val end = start + frames - 1
        return HttpResponse.ok(
            renderSequence(stage, sequence, sequenceChannels, song, start, end, preview = true)
        )
    }
}
