package io.sparkled.viewmodel.sequence

import io.sparkled.model.entity.Sequence
import io.sparkled.viewmodel.ModelConverter
import io.sparkled.viewmodel.ViewModelConverter

abstract class SequenceViewModelConverter : ModelConverter<Sequence, SequenceViewModel>,
    ViewModelConverter<SequenceViewModel, Sequence>
