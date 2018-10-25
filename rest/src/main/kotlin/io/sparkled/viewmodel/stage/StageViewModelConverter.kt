package io.sparkled.viewmodel.stage

import io.sparkled.model.entity.Stage
import io.sparkled.viewmodel.ModelConverter
import io.sparkled.viewmodel.ViewModelConverter

abstract class StageViewModelConverter : ModelConverter<Stage, StageViewModel>, ViewModelConverter<StageViewModel, Stage>
