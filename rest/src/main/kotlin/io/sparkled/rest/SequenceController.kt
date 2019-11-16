package io.sparkled.rest

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.QueryValue
import io.micronaut.spring.tx.annotation.Transactional
import io.sparkled.model.animation.SequenceChannelEffects
import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.model.entity.SequenceStatus
import io.sparkled.model.render.RenderResult
import io.sparkled.model.util.SequenceUtils
import io.sparkled.model.validator.exception.EntityNotFoundException
import io.sparkled.persistence.sequence.SequencePersistenceService
import io.sparkled.persistence.song.SongPersistenceService
import io.sparkled.persistence.stage.StagePersistenceService
import io.sparkled.renderer.Renderer
import io.sparkled.rest.response.IdResponse
import io.sparkled.viewmodel.sequence.SequenceViewModel
import io.sparkled.viewmodel.sequence.SequenceViewModelConverter
import io.sparkled.viewmodel.sequence.channel.SequenceChannelViewModelConverter
import io.sparkled.viewmodel.sequence.search.SequenceSearchViewModelConverter
import io.sparkled.viewmodel.stage.StageViewModelConverter
import io.sparkled.viewmodel.stage.prop.StagePropViewModelConverter
import java.util.UUID
import kotlin.math.min

@Controller("/api/sequences")
open class SequenceController(
    private val sequencePersistenceService: SequencePersistenceService,
    private val songPersistenceService: SongPersistenceService,
    private val stagePersistenceService: StagePersistenceService,
    private val sequenceSearchViewModelConverter: SequenceSearchViewModelConverter,
    private val sequenceViewModelConverter: SequenceViewModelConverter,
    private val sequenceChannelViewModelConverter: SequenceChannelViewModelConverter,
    private val stageViewModelConverter: StageViewModelConverter,
    private val stagePropViewModelConverter: StagePropViewModelConverter
) {

    @Get("/")
    @Transactional(readOnly = true)
    open fun getAllSequences(): HttpResponse<Any> {
        val sequences = sequencePersistenceService.getAllSequences()
        return HttpResponse.ok(sequenceSearchViewModelConverter.toViewModels(sequences))
    }

    @Get("/{id}")
    @Transactional(readOnly = true)
    open fun getSequence(id: Int): HttpResponse<Any> {
        val sequence = sequencePersistenceService.getSequenceById(id)

        if (sequence != null) {
            val viewModel = sequenceViewModelConverter.toViewModel(sequence)

            val channels = sequencePersistenceService
                .getSequenceChannelsBySequenceId(id)
                .asSequence()
                .map(sequenceChannelViewModelConverter::toViewModel)
                .toList()
            viewModel.setChannels(channels)

            return HttpResponse.ok(viewModel)
        }

        return HttpResponse.notFound("Sequence not found.")
    }

    @Get("/{id}/stage")
    @Transactional(readOnly = true)
    open fun getSequenceStage(id: Int): HttpResponse<Any> {
        val stage = sequencePersistenceService.getStageBySequenceId(id)

        return if (stage != null) {
            val viewModel = stageViewModelConverter.toViewModel(stage)
            val stageProps = stagePersistenceService
                .getStagePropsByStageId(stage.getId()!!)
                .asSequence()
                .map(stagePropViewModelConverter::toViewModel)
                .toList()

            viewModel.setStageProps(stageProps)
            HttpResponse.ok(viewModel)
        } else {
            HttpResponse.notFound("Stage not found for sequence.")
        }
    }

    @Get("/{id}/songAudio")
    @Transactional(readOnly = true)
    open fun getSequenceSongAudio(id: Int): MutableHttpResponse<out Any> {
        val songAudio = sequencePersistenceService.getSongAudioBySequenceId(id)
        return if (songAudio != null) {
            HttpResponse.ok(songAudio.getAudioData()!!).contentType("audio/mpeg")
        } else {
            HttpResponse.notFound("Song audio not found.")
        }
    }

    @Post("/")
    @Transactional
    open fun createSequence(sequenceViewModel: SequenceViewModel): HttpResponse<Any> {
        sequenceViewModel.setId(null)
        sequenceViewModel.setStatus(SequenceStatus.NEW)

        val sequence = sequenceViewModelConverter.toModel(sequenceViewModel)
        val sequenceChannels = createSequenceChannels(sequence)
        val savedSequence = sequencePersistenceService.saveSequence(sequence, sequenceChannels)

        return HttpResponse.ok(IdResponse(savedSequence.getId()!!))
    }

    private fun createSequenceChannels(sequence: Sequence): List<SequenceChannel> {
        val stageProps = stagePersistenceService.getStagePropsByStageId(sequence.getStageId()!!)
        return stageProps.mapIndexed { i, it ->
            SequenceChannel()
                .setUuid(UUID.randomUUID())
                .setName(it.getName())
                .setStagePropUuid(it.getUuid())
                .setDisplayOrder(i)
                .setChannelJson(SequenceChannelEffects.EMPTY_JSON)
        }
    }

    @Put("/{id}")
    @Transactional
    open fun updateSequence(id: Int, sequenceViewModel: SequenceViewModel): HttpResponse<Any> {
        sequenceViewModel.setId(id) // Prevent client-side ID tampering.

        val sequence = sequenceViewModelConverter.toModel(sequenceViewModel)
        val channels = sequenceViewModel.getChannels()
            .asSequence()
            .map(sequenceChannelViewModelConverter::toModel)
            .map { it.setSequenceId(id) }
            .toList()

        if (sequence.getStatus() === SequenceStatus.PUBLISHED) {
            publishSequence(sequence, channels)
        } else {
            saveDraftSequence(sequence, channels)
        }
        return HttpResponse.ok()
    }

    private fun publishSequence(sequence: Sequence, channels: List<SequenceChannel>) {
        val renderResult = renderSequence(sequence, channels)
        sequencePersistenceService.publishSequence(sequence, channels, renderResult.stageProps)
    }

    private fun saveDraftSequence(sequence: Sequence, channels: List<SequenceChannel>) {
        sequencePersistenceService.saveSequence(sequence, channels)
    }

    private fun renderSequence(
        sequence: Sequence,
        sequenceChannels: List<SequenceChannel>,
        startFrame: Int = 0,
        endFrame: Int = Int.MAX_VALUE
    ): RenderResult {
        val song = songPersistenceService.getSongBySequenceId(sequence.getId()!!)
            ?: throw EntityNotFoundException("Song not found.")

        val stageProps = stagePersistenceService.getStagePropsByStageId(sequence.getStageId()!!)
        val endFrameBounded = min(endFrame, SequenceUtils.getFrameCount(song, sequence) - 1)

        return Renderer(sequence, sequenceChannels, stageProps, startFrame, endFrameBounded).render()
    }

    @Delete("/{id}")
    @Transactional
    open fun deleteSequence(id: Int): HttpResponse<Any> {
        sequencePersistenceService.deleteSequence(id)
        return HttpResponse.ok()
    }

    @Post("/{id}/preview{?startFrame,frameCount}")
    @Transactional
    open fun previewSequence(
        request: HttpRequest<Any>,
        id: Int,
        @QueryValue(defaultValue = "0") startFrame: String,
        @QueryValue(defaultValue = "0") frameCount: String,
        sequenceViewModel: SequenceViewModel
    ): HttpResponse<Any> {
        val sequence = sequenceViewModelConverter.toModel(sequenceViewModel)
        val sequenceChannels = sequenceViewModel.getChannels()
            .asSequence()
            .map(sequenceChannelViewModelConverter::toModel)
            .toList()

        val start = startFrame.toInt()
        val frames = frameCount.toInt()

        val end = start + frames - 1
        return HttpResponse.ok(
            renderSequence(sequence, sequenceChannels, start, end)
        )
    }
}
