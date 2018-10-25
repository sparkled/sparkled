package io.sparkled.viewmodel.stage;

import io.sparkled.model.entity.Stage;
import io.sparkled.viewmodel.ModelConverter;
import io.sparkled.viewmodel.ViewModelConverter;

public abstract class StageViewModelConverter implements ModelConverter<Stage, StageViewModel>,
        ViewModelConverter<StageViewModel, Stage> {
}
