package io.sparkled.viewmodel.stage.prop

import io.sparkled.model.entity.StageProp
import io.sparkled.viewmodel.ModelConverter
import io.sparkled.viewmodel.ViewModelConverter

abstract class StagePropViewModelConverter : ModelConverter<StageProp, StagePropViewModel>,
    ViewModelConverter<StagePropViewModel, StageProp>
