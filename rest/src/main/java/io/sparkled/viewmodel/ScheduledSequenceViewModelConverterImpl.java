package io.sparkled.viewmodel;

import io.sparkled.model.entity.ScheduledSequence;
import io.sparkled.persistence.sequence.SequencePersistenceService;

import javax.inject.Inject;
import java.util.Optional;

public class ScheduledSequenceViewModelConverterImpl implements ScheduledSequenceViewModelConverter {

    private SequencePersistenceService sequencePersistenceService;

    @Inject
    public ScheduledSequenceViewModelConverterImpl(SequencePersistenceService sequencePersistenceService) {
        this.sequencePersistenceService = sequencePersistenceService;
    }

    @Override
    public ScheduledSequenceViewModel toViewModel(ScheduledSequence model) {
        ScheduledSequenceViewModel viewModel = new ScheduledSequenceViewModel();
        viewModel.setId(model.getId());
        viewModel.setStartTime(model.getStartTime());
        viewModel.setEndTime(model.getEndTime());

        Optional.of(model.getSequence()).ifPresent(sequence -> viewModel.setSequenceId(sequence.getId()));
        return viewModel;
    }

    @Override
    public ScheduledSequence fromViewModel(ScheduledSequenceViewModel viewModel) {
        ScheduledSequence model = new ScheduledSequence();
        model.setId(viewModel.getId());
        model.setStartTime(viewModel.getStartTime());
        model.setEndTime(viewModel.getEndTime());

        sequencePersistenceService.getSequenceById(viewModel.getSequenceId()).ifPresent(model::setSequence);

        return model;
    }
}
