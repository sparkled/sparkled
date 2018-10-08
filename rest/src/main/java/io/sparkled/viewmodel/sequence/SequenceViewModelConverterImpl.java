package io.sparkled.viewmodel.sequence;

import io.sparkled.model.entity.Sequence;
import io.sparkled.persistence.sequence.SequencePersistenceService;
import io.sparkled.viewmodel.exception.ViewModelConversionException;

import javax.inject.Inject;

public class SequenceViewModelConverterImpl implements SequenceViewModelConverter {

    private SequencePersistenceService sequencePersistenceService;

    @Inject
    public SequenceViewModelConverterImpl(SequencePersistenceService sequencePersistenceService) {
        this.sequencePersistenceService = sequencePersistenceService;
    }

    @Override
    public SequenceViewModel toViewModel(Sequence model) {
        return new SequenceViewModel()
                .setId(model.getId())
                .setStageId(model.getStageId())
                .setName(model.getName())
                .setArtist(model.getArtist())
                .setAlbum(model.getAlbum())
                .setDurationFrames(model.getDurationFrames())
                .setFramesPerSecond(model.getFramesPerSecond())
                .setStatus(model.getStatus());
    }

    @Override
    public Sequence fromViewModel(SequenceViewModel viewModel) {
        final Integer sequenceId = viewModel.getId();
        Sequence model = getSequence(sequenceId);

        return model
                .setId(viewModel.getId())
                .setStageId(viewModel.getStageId())
                .setName(viewModel.getName())
                .setArtist(viewModel.getArtist())
                .setAlbum(viewModel.getAlbum())
                .setDurationFrames(viewModel.getDurationFrames())
                .setFramesPerSecond(viewModel.getFramesPerSecond())
                .setStatus(viewModel.getStatus());
    }

    private Sequence getSequence(Integer sequenceId) {
        if (sequenceId == null) {
            return new Sequence();
        }

        return sequencePersistenceService.getSequenceById(sequenceId)
                .orElseThrow(() -> new ViewModelConversionException("Sequence with ID of '" + sequenceId + "' not found."));
    }
}
