package io.sparkled.viewmodel.converter;

import io.sparkled.model.entity.ScheduledSequence;
import io.sparkled.viewmodel.ScheduledSequenceViewModel;

public interface ScheduledSequenceViewModelConverter extends ViewModelConverter<ScheduledSequenceViewModel, ScheduledSequence> {

    @Override
    ScheduledSequenceViewModel toViewModel(ScheduledSequence model);

    @Override
    ScheduledSequence fromViewModel(ScheduledSequenceViewModel viewModel);
}
