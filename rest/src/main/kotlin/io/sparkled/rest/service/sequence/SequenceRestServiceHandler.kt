package io.sparkled.rest.service.sequence

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
import io.sparkled.persistence.transaction.Transaction
import io.sparkled.renderer.Renderer
import io.sparkled.rest.response.IdResponse
import io.sparkled.rest.service.RestServiceHandler
import io.sparkled.viewmodel.sequence.SequenceViewModel
import io.sparkled.viewmodel.sequence.SequenceViewModelConverter
import io.sparkled.viewmodel.sequence.channel.SequenceChannelViewModelConverter
import io.sparkled.viewmodel.sequence.search.SequenceSearchViewModelConverter
import io.sparkled.viewmodel.stage.StageViewModelConverter
import io.sparkled.viewmodel.stage.prop.StagePropViewModelConverter
import org.slf4j.LoggerFactory
import java.util.UUID
import javax.inject.Inject
import javax.ws.rs.core.Response
import kotlin.math.min

open class SequenceRestServiceHandler
@Inject constructor(
    private val transaction: Transaction,
    private val sequencePersistenceService: SequencePersistenceService,
    private val songPersistenceService: SongPersistenceService,
    private val stagePersistenceService: StagePersistenceService,
    private val sequenceViewModelConverter: SequenceViewModelConverter,
    private val sequenceSearchViewModelConverter: SequenceSearchViewModelConverter,
    private val sequenceChannelViewModelConverter: SequenceChannelViewModelConverter,
    private val stageViewModelConverter: StageViewModelConverter,
    private val stagePropViewModelConverter: StagePropViewModelConverter
) : RestServiceHandler() {

    fun createSequence(sequenceViewModel: SequenceViewModel): Response {
        return transaction.of {
            sequenceViewModel.setId(null)
            sequenceViewModel.setStatus(SequenceStatus.NEW)

            val sequence = sequenceViewModelConverter.toModel(sequenceViewModel)
            val sequenceChannels = createSequenceChannels(sequence)
            val savedSequence = sequencePersistenceService.saveSequence(sequence, sequenceChannels)

            return@of respondOk(IdResponse(savedSequence.getId()!!))
        }
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

    fun getAllSequences(): Response {
        val sequences = sequencePersistenceService.getAllSequences()
        val viewModels = sequenceSearchViewModelConverter.toViewModels(sequences)
        return respondOk(viewModels)
    }

    fun getSequence(sequenceId: Int): Response {
        val sequence = sequencePersistenceService.getSequenceById(sequenceId)

        if (sequence != null) {
            val viewModel = sequenceViewModelConverter.toViewModel(sequence)

            val channels = sequencePersistenceService
                .getSequenceChannelsBySequenceId(sequenceId)
                .asSequence()
                .map(sequenceChannelViewModelConverter::toViewModel)
                .toList()
            viewModel.setChannels(channels)

            return respondOk(viewModel)
        }

        return respond(Response.Status.NOT_FOUND, "Sequence not found.")
    }

    fun getSequenceStage(sequenceId: Int): Response {
        val stage = sequencePersistenceService.getStageBySequenceId(sequenceId)

        return if (stage != null) {
            val viewModel = stageViewModelConverter.toViewModel(stage)
            val stageProps = stagePersistenceService
                .getStagePropsByStageId(stage.getId()!!)
                .asSequence()
                .map(stagePropViewModelConverter::toViewModel)
                .toList()

            viewModel.setStageProps(stageProps)
            respondOk(viewModel)
        } else {
            respond(Response.Status.NOT_FOUND, "Stage not found.")
        }
    }

    fun getSequenceSongAudio(sequenceId: Int): Response {
        val songAudio = sequencePersistenceService.getSongAudioBySequenceId(sequenceId)
        return if (songAudio != null) {
            respondMedia(songAudio.getAudioData()!!, MP3_MIME_TYPE)
        } else {
            respond(Response.Status.NOT_FOUND, "Song audio not found.")
        }
    }

    fun previewSequence(
        sequenceID: Int,
        startFrame: Int,
        frameCount: Int,
        sequenceViewModel: SequenceViewModel
    ): Response {
        logger.info("Previewing sequence #$sequenceID.")
        val sequence = sequenceViewModelConverter.toModel(sequenceViewModel)

        val sequenceChannels = sequenceViewModel.getChannels()
            .asSequence()
            .map(sequenceChannelViewModelConverter::toModel)
            .toList()

        return respondOk(
            renderSequence(sequence, sequenceChannels, startFrame, startFrame + frameCount - 1)
        )
    }

    fun updateSequence(id: Int, sequenceViewModel: SequenceViewModel): Response {
        return transaction.of {
            sequenceViewModel.setId(id) // Prevent client-side ID tampering.

            val sequence = sequenceViewModelConverter.toModel(sequenceViewModel)
            val sequenceChannels = sequenceViewModel.getChannels()
                .asSequence()
                .map(sequenceChannelViewModelConverter::toModel)
                .toList()

            if (sequence.getStatus() === SequenceStatus.PUBLISHED) {
                publishSequence(sequence, sequenceChannels)
            } else {
                saveDraftSequence(sequence, sequenceChannels)
            }

            return@of respondOk()
        }
    }

    fun deleteSequence(id: Int): Response {
        return transaction.of {
            sequencePersistenceService.deleteSequence(id)
            return@of respondOk()
        }
    }

    private fun saveDraftSequence(sequence: Sequence, sequenceChannels: List<SequenceChannel>) {
        sequencePersistenceService.saveSequence(sequence, sequenceChannels)
    }

    private fun publishSequence(sequence: Sequence, sequenceChannels: List<SequenceChannel>) {
        val renderResult = renderSequence(sequence, sequenceChannels)
        sequencePersistenceService.publishSequence(sequence, sequenceChannels, renderResult.stageProps)
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

    companion object {
        private const val MP3_MIME_TYPE = "audio/mpeg"
        private val logger = LoggerFactory.getLogger(SequenceRestServiceHandler::class.java)
    }
}