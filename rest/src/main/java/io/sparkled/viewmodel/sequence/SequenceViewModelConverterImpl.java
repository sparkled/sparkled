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
                .setName(model.getName())
                .setDurationFrames(model.getDurationFrames())
                .setFramesPerSecond(model.getFramesPerSecond());
    }

    @Override
    public Sequence fromViewModel(SequenceViewModel viewModel) {
        final Integer sequenceId = viewModel.getId();
        Sequence model = sequencePersistenceService.getSequenceById(sequenceId)
                .orElseThrow(() -> new ViewModelConversionException("Sequence with ID of '" + sequenceId + "' not found."));

        return model.setName(viewModel.getName());
    }
}
