package io.sparkled.rest.service.sequence

import com.google.inject.persist.Transactional
import io.sparkled.model.entity.*
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.model.util.SequenceUtils
import io.sparkled.model.validator.exception.EntityNotFoundException
import io.sparkled.persistence.sequence.SequencePersistenceService
import io.sparkled.persistence.song.SongPersistenceService
import io.sparkled.persistence.stage.StagePersistenceService
import io.sparkled.renderer.Renderer
import io.sparkled.rest.response.IdResponse
import io.sparkled.rest.service.RestServiceHandler
import io.sparkled.viewmodel.sequence.SequenceViewModel
import io.sparkled.viewmodel.sequence.SequenceViewModelConverter
import io.sparkled.viewmodel.sequence.channel.SequenceChannelViewModel
import io.sparkled.viewmodel.sequence.channel.SequenceChannelViewModelConverter
import io.sparkled.viewmodel.sequence.search.SequenceSearchViewModel
import io.sparkled.viewmodel.sequence.search.SequenceSearchViewModelConverter
import io.sparkled.viewmodel.stage.StageViewModel
import io.sparkled.viewmodel.stage.StageViewModelConverter
import io.sparkled.viewmodel.stage.prop.StagePropViewModel
import io.sparkled.viewmodel.stage.prop.StagePropViewModelConverter

import javax.inject.Inject
import javax.ws.rs.core.Response
import java.util.Optional

import java.util.stream.Collectors.toList

internal class SequenceRestServiceHandler @Inject
constructor(private val sequencePersistenceService: SequencePersistenceService,
            private val songPersistenceService: SongPersistenceService,
            private val stagePersistenceService: StagePersistenceService,
            private val sequenceViewModelConverter: SequenceViewModelConverter,
            private val sequenceSearchViewModelConverter: SequenceSearchViewModelConverter,
            private val sequenceChannelViewModelConverter: SequenceChannelViewModelConverter,
            private val stageViewModelConverter: StageViewModelConverter,
            private val stagePropViewModelConverter: StagePropViewModelConverter) : RestServiceHandler() {

    @Transactional
    fun createSequence(sequenceViewModel: SequenceViewModel): Response {
        sequenceViewModel.setId(null)
        sequenceViewModel.setStatus(SequenceStatus.NEW)

        var sequence = sequenceViewModelConverter.toModel(sequenceViewModel)
        sequence = sequencePersistenceService.createSequence(sequence)
        return respondOk(IdResponse(sequence.getId()))
    }

    val allSequences: Response
        get() {
            val sequences = sequencePersistenceService.getAllSequences()
            val viewModels = sequenceSearchViewModelConverter.toViewModels(sequences)
            return respondOk(viewModels)
        }

    fun getSequence(sequenceId: Int): Response {
        val sequenceOptional = sequencePersistenceService.getSequenceById(sequenceId)

        if (sequenceOptional.isPresent()) {
            val sequence = sequenceOptional.get()
            val viewModel = sequenceViewModelConverter.toViewModel(sequence)

            val channels = sequencePersistenceService
                    .getSequenceChannelsBySequenceId(sequenceId)
                    .stream()
                    .map(???({ sequenceChannelViewModelConverter.toViewModel() }))
            .collect(toList())
            viewModel.setChannels(channels)

            return respondOk(viewModel)
        }

        return respond(Response.Status.NOT_FOUND, "Sequence not found.")
    }

    fun getSequenceStage(sequenceId: Int): Response {
        val stageOptional = sequencePersistenceService.getStageBySequenceId(sequenceId)

        if (stageOptional.isPresent()) {
            val stage = stageOptional.get()

            val viewModel = stageViewModelConverter.toViewModel(stage)
            val stageProps = stagePersistenceService
                    .getStagePropsByStageId(stage.getId())
                    .stream()
                    .map(???({ stagePropViewModelConverter.toViewModel() }))
            .collect(toList())
            viewModel.setStageProps(stageProps)
            return respondOk(viewModel)
        } else {
            return respond(Response.Status.NOT_FOUND, "Stage not found.")
        }
    }

    fun getSequenceSongAudio(sequenceId: Int): Response {
        val songAudioOptional = sequencePersistenceService.getSongAudioBySequenceId(sequenceId)
        if (songAudioOptional.isPresent()) {
            return respondMedia(songAudioOptional.get().getAudioData(), MP3_MIME_TYPE)
        } else {
            return respond(Response.Status.NOT_FOUND, "Song audio not found.")
        }
    }

    @Transactional
    fun updateSequence(id: Int, sequenceViewModel: SequenceViewModel): Response {
        sequenceViewModel.setId(id) // Prevent client-side ID tampering.

        val sequence = sequenceViewModelConverter.toModel(sequenceViewModel)
        val sequenceChannels = sequenceViewModel.getChannels()
                .stream()
                .map(???({ sequenceChannelViewModelConverter.toModel() }))
        .collect(toList())

        if (sequence.getStatus() === SequenceStatus.PUBLISHED) {
            publishSequence(sequence, sequenceChannels)
        } else {
            saveDraftSequence(sequence, sequenceChannels)
        }

        return respondOk()
    }

    @Transactional
    fun deleteSequence(id: Int): Response {
        sequencePersistenceService.deleteSequence(id)
        return respondOk()
    }

    private fun saveDraftSequence(sequence: Sequence, sequenceChannels: List<SequenceChannel>) {
        sequencePersistenceService.saveSequence(sequence, sequenceChannels)
    }

    private fun publishSequence(sequence: Sequence, sequenceChannels: List<SequenceChannel>) {
        val song = songPersistenceService.getSongBySequenceId(sequence.getId())
                .orElseThrow({ EntityNotFoundException("Song not found.") })

        val stageProps = stagePersistenceService.getStagePropsByStageId(sequence.getStageId())
        val endFrame = SequenceUtils.getFrameCount(song, sequence) - 1

        val renderedStageProps = Renderer(sequence, sequenceChannels, stageProps, 0, endFrame).render()
        sequencePersistenceService.publishSequence(sequence, sequenceChannels, renderedStageProps)
    }

    companion object {

        private val MP3_MIME_TYPE = "audio/mpeg"
    }
}