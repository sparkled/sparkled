package io.sparkled.api

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
import io.micronaut.transaction.annotation.Transactional
import io.sparkled.model.SequenceChannelModel
import io.sparkled.model.SequenceModel
import io.sparkled.model.SongModel
import io.sparkled.model.StageModel
import io.sparkled.model.UniqueId
import io.sparkled.model.enumeration.SequenceStatus
import io.sparkled.model.render.RenderResult
import io.sparkled.model.render.RenderedSequence
import io.sparkled.model.render.RenderedSequenceStageProp
import io.sparkled.model.util.IdUtils.uniqueId
import io.sparkled.model.util.SequenceUtils
import io.sparkled.persistence.DbService
import io.sparkled.persistence.FileService
import io.sparkled.persistence.cache.CacheService
import io.sparkled.persistence.repository.findByIdOrNull
import io.sparkled.renderer.RenderMode
import io.sparkled.renderer.Renderer
import io.sparkled.renderer.SparkledPluginManager
import io.sparkled.viewmodel.SequenceEditViewModel
import io.sparkled.viewmodel.SequenceSummaryViewModel
import io.sparkled.viewmodel.SequenceViewModel
import io.sparkled.viewmodel.StageViewModel
import io.sparkled.viewmodel.error.ApiErrorCode
import io.sparkled.viewmodel.exception.HttpResponseException
import java.time.Instant
import java.util.Base64
import kotlin.math.min

@ExecuteOn(TaskExecutors.BLOCKING)
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/sequences")
class SequenceController(
    private val cache: CacheService,
    private val db: DbService,
    private val fileService: FileService,
    private val pluginManager: SparkledPluginManager,
) {

    @Get("/")
    @Transactional
    fun getAllSequences(): HttpResponse<Any> {
        val songs = db.songs.findAll().associateBy { it.id }
        val stages = db.stages.findAll().associateBy { it.id }

        val viewModels = db.sequences.findAll()
            .sortedBy { it.name }
            .map { SequenceSummaryViewModel.fromModel(it, songs.getValue(it.songId), stages.getValue(it.stageId)) }

        return HttpResponse.ok(viewModels)
    }

    @Get("/{id}")
    @Transactional
    fun getSequence(id: UniqueId): HttpResponse<Any> {
        val viewModel = db.sequences.findByIdOrNull(id)?.let {
            val song = db.songs.findBySequenceId(it.id)
                ?: throw HttpResponseException(ApiErrorCode.ERR_NOT_FOUND)
            val channels = db.sequenceChannels.findAllBySequenceId(it.id)
            SequenceViewModel.fromModel(it, song, channels)
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
            StageViewModel.fromModel(it, stageProps)
        }

        return when {
            viewModel == null -> HttpResponse.notFound("Stage not found for sequence.")
            else -> HttpResponse.ok(viewModel)
        }
    }

    @Get("/{id}/songAudio")
    @Transactional
    fun getSequenceSongAudio(id: UniqueId): MutableHttpResponse<out Any> {
        val songAudio = db.songs.findBySequenceId(id)?.let { cache.songAudios.get()[it.id] }
        return if (songAudio != null) {
            HttpResponse.ok(songAudio).contentType("audio/mpeg")
        } else HttpResponse.notFound("Song audio not found.")
    }

    @Post("/")
    @Transactional
    fun createSequence(
        @Body body: SequenceEditViewModel,
    ): HttpResponse<SequenceViewModel> {
        val sequence = SequenceModel(
            stageId = body.stageId,
            songId = body.songId,
            status = body.status,
            name = body.name,
            framesPerSecond = body.framesPerSecond,
        )

        val saved = db.sequences.save(sequence)

        val sequenceChannels = createDefaultSequenceChannels(sequence)
        val savedChannels = db.sequenceChannels.saveAll(sequenceChannels).toList()

        val song = db.songs.findByIdOrNull(saved.songId)!!
        val viewModel = SequenceViewModel.fromModel(saved, song, savedChannels)
        return HttpResponse.created(viewModel)
    }

    private fun createDefaultSequenceChannels(sequence: SequenceModel): List<SequenceChannelModel> {
        val stageProps = db.stageProps.findAllByStageId(sequence.stageId)
        return stageProps.mapIndexed { i, it ->
            SequenceChannelModel(
                id = uniqueId(),
                sequenceId = sequence.id,
                stagePropId = it.id,
                name = it.name,
                displayOrder = i,
            )
        }
    }

    @Put("/{id}")
    @Transactional
    fun updateSequence(
        @PathVariable id: UniqueId,
        @Body body: SequenceEditViewModel,
    ): HttpResponse<Any> {
        val sequence = db.sequences.findByIdOrNull(id)
            ?: throw HttpResponseException(ApiErrorCode.ERR_NOT_FOUND)

        val editedSequence = sequence.copy(
            name = body.name,
            framesPerSecond = body.framesPerSecond,
            status = body.status,
        )
        val sequenceChannels = body.channels.map { it.toModel(id) }

        val newChannels = sequenceChannels.associateBy { it.id }
        val existingChannels = db.sequenceChannels.findAllBySequenceId(id).associateBy { it.id }

        // Delete channels that no longer exist.
        val toDelete = existingChannels.keys - newChannels.keys
        toDelete.forEach { db.sequenceChannels.deleteById(it) }

        // Insert channels that didn't exist previously.
        val toCreate = newChannels.keys - existingChannels.keys
        toCreate.forEach { db.sequenceChannels.save(newChannels.getValue(it)) }

        // Update channels that exist
        val toUpdate = newChannels.keys.intersect(existingChannels.keys)
        toUpdate.forEach { db.sequenceChannels.update(newChannels.getValue(it)) }

        if (editedSequence.status === SequenceStatus.PUBLISHED) {
            db.sequences.update(editedSequence)
            val stage = db.stages.findBySequenceId(id) ?: throw HttpResponseException(ApiErrorCode.ERR_NOT_FOUND)
            val song = db.songs.findBySequenceId(id) ?: throw HttpResponseException(ApiErrorCode.ERR_NOT_FOUND)

            val renderResult = renderSequence(stage, editedSequence, sequenceChannels, song, preview = false)
            val renderedSequence = RenderedSequence(
                sequenceId = editedSequence.id,
                startFrame = renderResult.startFrame,
                frameCount = renderResult.frameCount,
                stageProps = renderResult.stageProps.mapValues {
                    RenderedSequenceStageProp(
                        ledCount = it.value.ledCount,
                        base64Data = Base64.getEncoder().encodeToString(it.value.data)
                    )
                }
            )
            fileService.writeRender(sequence.id, renderedSequence)
        } else {
            db.sequences.update(sequence.copy(status = SequenceStatus.DRAFT, updatedAt = Instant.now()))
        }

        return HttpResponse.ok()
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
            { cache.gifs.get() },
            stage,
            sequence.framesPerSecond,
            sequenceChannels.associate { it.stagePropId to it.channelData },
            stageProps.associateBy { it.id },
            startFrame,
            endFrameBounded,
            if (preview) RenderMode.PREVIEW_SEQUENCE else RenderMode.PUBLISH_SEQUENCE,
        ).render()
    }

    @Post("/{id}/preview{?startFrame,frameCount}")
    @Transactional
    fun previewSequence(
        @PathVariable id: UniqueId,
        @QueryValue(defaultValue = "0") startFrame: Int,
        @QueryValue(defaultValue = "0") frameCount: Int,
        @Body body: SequenceEditViewModel,
    ): HttpResponse<Any> {
        val sequence = db.sequences.findByIdOrNull(id)
            ?: throw HttpResponseException(ApiErrorCode.ERR_NOT_FOUND)

        val sequenceChannels = body.channels.map { it.toModel(id) }

        val stage = db.stages.findBySequenceId(id) ?: throw HttpResponseException(ApiErrorCode.ERR_NOT_FOUND)
        val song = db.songs.findBySequenceId(id) ?: throw HttpResponseException(ApiErrorCode.ERR_NOT_FOUND)

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
