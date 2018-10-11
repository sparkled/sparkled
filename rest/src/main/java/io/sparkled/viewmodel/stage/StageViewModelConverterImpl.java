package io.sparkled.viewmodel.stage;

import io.sparkled.model.entity.Stage;
import io.sparkled.persistence.stage.StagePersistenceService;
import io.sparkled.viewmodel.exception.ViewModelConversionException;

import javax.inject.Inject;

public class StageViewModelConverterImpl extends StageViewModelConverter {

    private StagePersistenceService stagePersistenceService;

    @Inject
    public StageViewModelConverterImpl(StagePersistenceService stagePersistenceService) {
        this.stagePersistenceService = stagePersistenceService;
    }

    @Override
    public StageViewModel toViewModel(Stage model) {
        return new StageViewModel()
                .setId(model.getId())
                .setName(model.getName())
                .setWidth(model.getWidth())
                .setHeight(model.getHeight());
    }

    @Override
    public Stage toModel(StageViewModel viewModel) {
        final Integer stageId = viewModel.getId();
        Stage model = getStage(stageId);

        return model
                .setName(viewModel.getName())
                .setWidth(viewModel.getWidth())
                .setHeight(viewModel.getHeight());
    }

    private Stage getStage(Integer stageId) {
        if (stageId == null) {
            return new Stage();
        }

        return stagePersistenceService.getStageById(stageId)
                .orElseThrow(() -> new ViewModelConversionException("Stage with ID of '" + stageId + "' not found."));
    }
}
