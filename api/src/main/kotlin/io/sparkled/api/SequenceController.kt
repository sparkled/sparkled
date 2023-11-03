package io.sparkled.api

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.QueryValue
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.sparkled.model.SequenceChannelModel
import io.sparkled.model.SequenceModel
import io.sparkled.model.SongModel
import io.sparkled.model.StageModel
import io.sparkled.model.UniqueId
import io.sparkled.model.animation.SequenceChannelEffects
import io.sparkled.model.enumeration.SequenceStatus
import io.sparkled.model.render.RenderResult
import io.sparkled.model.render.RenderedSequence
import io.sparkled.model.render.RenderedSequenceStageProp
import io.sparkled.model.util.IdUtils.uniqueId
import io.sparkled.model.util.SequenceUtils
import io.sparkled.model.validator.exception.EntityNotFoundException
import io.sparkled.persistence.DbService
import io.sparkled.persistence.FileService
import io.sparkled.persistence.cache.CacheService
import io.sparkled.persistence.repository.findByIdOrNull
import io.sparkled.renderer.Renderer
import io.sparkled.renderer.SparkledPluginManager
import io.sparkled.viewmodel.SequenceEditViewModel
import io.sparkled.viewmodel.SequenceSummaryViewModel
import io.sparkled.viewmodel.SequenceViewModel
import io.sparkled.viewmodel.StageViewModel
import jakarta.transaction.Transactional
import java.time.Instant
import java.util.Base64
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
    @Transactional
    fun getAllSequences(): HttpResponse<Any> {
        val songs = db.songs.findAll().associateBy { it.id }
        val stages = db.stages.findAll().associateBy { it.id }

        val viewModels = db.sequences.findAll()
            .sortedBy { it.name }
            .map { SequenceSummaryViewModel.fromModel(it, songs.getValue(it.id), stages.getValue(it.id)) }

        return HttpResponse.ok(viewModels)
    }

    @Get("/{id}")
    @Transactional
    fun getSequence(id: UniqueId): HttpResponse<Any> {
        val viewModel = db.sequences.findByIdOrNull(id)?.let {
            val song = db.songs.findBySequenceId(it.id)!!
            val channels = db.sequenceChannels.findAllBySequenceId(it.id)
            SequenceViewModel.fromModel(it, song, channels, objectMapper)
        }

        return if (viewModel != null) {
            HttpResponse.ok(viewModel)
        } else {
            HttpResponse.notFound("Sequence not found.")
        }
    }

    @Get("/{id}/stage")
    @Transactional
    fun getSequenceStage(id: UniqueId): HttpResponse<Any> {
        val viewModel = db.stages.findBySequenceId(id)?.let {
            val stageProps = db.stageProps.findAllByStageId(it.id)
            StageViewModel.fromModel(it, stageProps, objectMapper)
        }

        return when {
            viewModel == null -> HttpResponse.notFound("Stage not found for sequence.")
            else -> HttpResponse.ok(viewModel)
        }
    }

    @Get("/{id}/songAudio")
    @Transactional
    fun getSequenceSongAudio(id: UniqueId): MutableHttpResponse<out Any> {
        val songAudio = db.songs.findBySequenceId(id)?.let { file.readSongAudio(it.id) }
        return if (songAudio != null) {
            HttpResponse.ok(songAudio).contentType("audio/mpeg")
        } else HttpResponse.notFound("Song audio not found.")
    }

    @Post("/")
    @Transactional
    fun createSequence(sequenceViewModel: SequenceViewModel): HttpResponse<Any> {
        val (sequence) = sequenceViewModel.copy(status = SequenceStatus.NEW).toModel(objectMapper)
        val saved = db.sequences.save(sequence)

        val sequenceChannels = createSequenceChannels(sequence.copy(id = saved.id))

        db.sequenceChannels.saveAll(
            sequenceChannels.map { it.copy(id = uniqueId(), sequenceId = saved.id) }
        )

        return HttpResponse.ok(saved) // TODO viewmodel
    }

    private fun createSequenceChannels(sequence: SequenceModel): List<SequenceChannelModel> {
        val stageProps = db.stageProps.findAllByStageId(sequence.stageId)
        return stageProps.mapIndexed { i, it ->
            SequenceChannelModel(
                id = uniqueId(),
                sequenceId = sequence.id,
                stagePropId = it.id,
                name = it.name,
                displayOrder = i,
                channelJson = SequenceChannelEffects.EMPTY_JSON
            )
        }
    }

    @Put("/{id}")
    @Transactional
    fun updateSequence(
        @PathVariable id: UniqueId,
        @Body body: SequenceEditViewModel,
    ): HttpResponse<Any> {
        TODO()
//
//        val sequenceAndChannels = body.copy(id = id).toModel(objectMapper)
//        val sequence = sequenceAndChannels.first.copy(id = id)
//        val sequenceChannels = sequenceAndChannels.second.map { it.copy(sequenceId = id) }
//
//        val newChannels = sequenceChannels.associateBy { it.id }
//        val existingChannels = db.sequenceChannels.findAllBySequenceId(id).associateBy { it.id }
//
//        // Delete channels that no longer exist.
//        val toDelete = existingChannels.keys - newChannels.keys
//        toDelete.forEach { db.sequenceChannels.deleteById(it) }
//
//        // Insert channels that didn't exist previously.
//        val toCreate = newChannels.keys - existingChannels.keys
//        toCreate.forEach { db.sequences.save(newChannels.getValue(it)) }
//
//        // Update channels that exist
//        val toUpdate = newChannels.keys.intersect(existingChannels.keys)
//        toUpdate.forEach { db.sequences.update(newChannels.getValue(it)) }
//
//        if (sequence.status === SequenceStatus.PUBLISHED) {
//            db.sequences.update(sequence)
//            val stage = db.stages.findBySequenceId(id) ?: throw EntityNotFoundException("Stage not found.")
//            val song = db.songs.findBySequenceId(id) ?: throw EntityNotFoundException("Song not found.")
//
//            val renderResult = renderSequence(stage, sequence, sequenceChannels, song, preview = false)
//            val renderedSequence = RenderedSequence(
//                sequenceId = sequence.id,
//                startFrame = renderResult.startFrame,
//                frameCount = renderResult.frameCount,
//                stageProps = renderResult.stageProps.mapValues {
//                    RenderedSequenceStageProp(
//                        ledCount = it.value.ledCount,
//                        base64Data = Base64.getEncoder().encodeToString(it.value.data)
//                    )
//                }
//            )
//            file.writeRender(sequence.id, renderedSequence)
//        } else {
//            db.sequences.update(sequence.copy(status = SequenceStatus.DRAFT, updatedAt = Instant.now()))
//        }
//
//        return HttpResponse.ok()
    }

    private fun renderSequence(
        stage: StageModel,
        sequence: SequenceModel,
        sequenceChannels: List<SequenceChannelModel>,
        song: SongModel,
        startFrame: Int = 0,
        endFrame: Int = Int.MAX_VALUE,
        preview: Boolean,
    ): RenderResult {
        val stageProps = db.stageProps.findAllByStageId(sequence.stageId)
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

    @Post("/{id}/preview{?startFrame,frameCount}")
    @Transactional
    fun previewSequence(
        @PathVariable id: UniqueId,
        @QueryValue(defaultValue = "0") startFrame: Int,
        @QueryValue(defaultValue = "0") frameCount: Int,
        sequenceViewModel: SequenceViewModel
    ): HttpResponse<Any> {
        val (sequence, sequenceChannels) = sequenceViewModel.toModel(objectMapper)
        val stage = db.stages.findBySequenceId(id) ?: throw EntityNotFoundException("Stage not found.")
        val song = db.songs.findBySequenceId(id) ?: throw EntityNotFoundException("Song not found.")

        val end = startFrame + frameCount - 1
        return HttpResponse.ok(
            renderSequence(stage, sequence, sequenceChannels, song, startFrame, end, preview = true)
        )
    }

    @Delete("/{id}")
    @Transactional
    fun deleteSequence(
        @PathVariable id: UniqueId,
    ): HttpResponse<Any> {
        db.sequences.deleteSequenceAndDependentsById(id)
        return HttpResponse.noContent()
    }
}
