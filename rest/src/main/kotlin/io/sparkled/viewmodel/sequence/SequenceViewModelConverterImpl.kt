package io.sparkled.viewmodel.sequence

import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.Song
import io.sparkled.model.util.SequenceUtils
import io.sparkled.persistence.sequence.SequencePersistenceService
import io.sparkled.persistence.song.SongPersistenceService
import io.sparkled.viewmodel.exception.ViewModelConversionException

import javax.inject.Inject

class SequenceViewModelConverterImpl @Inject
constructor(
    private val sequencePersistenceService: SequencePersistenceService,
    private val songPersistenceService: SongPersistenceService
) : SequenceViewModelConverter() {

    override fun toViewModel(model: Sequence): SequenceViewModel {
        return SequenceViewModel()
            .setId(model.getId())
            .setSongId(model.getSongId())
            .setStageId(model.getStageId())
            .setName(model.getName())
            .setFramesPerSecond(model.getFramesPerSecond())
            .setFrameCount(getFrameCount(model))
            .setStatus(model.getStatus())
    }

    private fun getFrameCount(sequence: Sequence): Int {
        val song = songPersistenceService.getSongBySequenceId(sequence.getId()!!) ?: Song()
        return SequenceUtils.getFrameCount(song, sequence)
    }

    override fun toModel(viewModel: SequenceViewModel): Sequence {
        val sequenceId = viewModel.getId()
        val model = getSequence(sequenceId)

        return model
            .setSongId(viewModel.getSongId())
            .setStageId(viewModel.getStageId())
            .setName(viewModel.getName())
            .setFramesPerSecond(viewModel.getFramesPerSecond())
            .setStatus(viewModel.getStatus())
    }

    private fun getSequence(sequenceId: Int?): Sequence {
        if (sequenceId == null) {
            return Sequence()
        }

        return sequencePersistenceService.getSequenceById(sequenceId)
            ?: throw ViewModelConversionException("Sequence with ID of '$sequenceId' not found.")
    }
}
