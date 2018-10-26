package io.sparkled.rest.service.sequence

import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.model.entity.SequenceStatus
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
import javax.inject.Inject
import javax.inject.Provider
import javax.persistence.EntityManager
import javax.ws.rs.core.Response

open class SequenceRestServiceHandler @Inject
constructor(
    private val entityManagerProvider: Provider<EntityManager>,
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
        return Transaction(entityManagerProvider).of {
            sequenceViewModel.setId(null)
            sequenceViewModel.setStatus(SequenceStatus.NEW)

            val sequence = sequenceViewModelConverter.toModel(sequenceViewModel)
            val savedSequence = sequencePersistenceService.createSequence(sequence)
            return@of respondOk(IdResponse(savedSequence.getId()!!))
        }
    }

    fun getAllSequences(): Response {
        val sequences = sequencePersistenceService.getAllSequences()
        val viewModels = sequenceSearchViewModelConverter.toViewModels(sequences)
        return respondOk(viewModels)
    }

    fun getSequence(sequenceId: Int): Response {
        val sequenceOptional = sequencePersistenceService.getSequenceById(sequenceId)

        if (sequenceOptional.isPresent) {
            val sequence = sequenceOptional.get()
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
        val stageOptional = sequencePersistenceService.getStageBySequenceId(sequenceId)

        return if (stageOptional.isPresent) {
            val stage = stageOptional.get()

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
        val songAudioOptional = sequencePersistenceService.getSongAudioBySequenceId(sequenceId)
        return if (songAudioOptional.isPresent) {
            respondMedia(songAudioOptional.get().getAudioData()!!, MP3_MIME_TYPE)
        } else {
            respond(Response.Status.NOT_FOUND, "Song audio not found.")
        }
    }

    fun updateSequence(id: Int, sequenceViewModel: SequenceViewModel): Response {
        return Transaction(entityManagerProvider).of {
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
        return Transaction(entityManagerProvider).of {
            sequencePersistenceService.deleteSequence(id)
            return@of respondOk()
        }
    }

    private fun saveDraftSequence(sequence: Sequence, sequenceChannels: List<SequenceChannel>) {
        sequencePersistenceService.saveSequence(sequence, sequenceChannels)
    }

    private fun publishSequence(sequence: Sequence, sequenceChannels: List<SequenceChannel>) {
        val song = songPersistenceService.getSongBySequenceId(sequence.getId()!!)
                .orElseThrow { EntityNotFoundException("Song not found.") }

        val stageProps = stagePersistenceService.getStagePropsByStageId(sequence.getStageId()!!)
        val endFrame = SequenceUtils.getFrameCount(song, sequence) - 1

        val renderedStageProps = Renderer(sequence, sequenceChannels, stageProps, 0, endFrame).render()
        sequencePersistenceService.publishSequence(sequence, sequenceChannels, renderedStageProps)
    }

    companion object {
        private const val MP3_MIME_TYPE = "audio/mpeg"
    }
}