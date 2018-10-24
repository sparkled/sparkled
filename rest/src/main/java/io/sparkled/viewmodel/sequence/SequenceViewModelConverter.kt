package io.sparkled.viewmodel.sequence;

import io.sparkled.model.entity.Sequence;
import io.sparkled.viewmodel.ModelConverter;
import io.sparkled.viewmodel.ViewModelConverter;

public abstract class SequenceViewModelConverter implements ModelConverter<Sequence, SequenceViewModel>,
        ViewModelConverter<SequenceViewModel, Sequence> {
}
