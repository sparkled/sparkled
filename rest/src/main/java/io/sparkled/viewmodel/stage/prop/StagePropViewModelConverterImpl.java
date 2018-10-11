package io.sparkled.viewmodel.stage.prop;

import io.sparkled.model.entity.StageProp;
import io.sparkled.persistence.stage.StagePersistenceService;

import javax.inject.Inject;

public class StagePropViewModelConverterImpl extends StagePropViewModelConverter {

    private StagePersistenceService stagePersistenceService;

    @Inject
    public StagePropViewModelConverterImpl(StagePersistenceService stagePersistenceService) {
        this.stagePersistenceService = stagePersistenceService;
    }

    @Override
    public StagePropViewModel toViewModel(StageProp model) {
        return new StagePropViewModel()
                .setUuid(model.getUuid())
                .setStageId(model.getStageId())
                .setCode(model.getCode())
                .setName(model.getName())
                .setType(model.getType())
                .setLedCount(model.getLedCount())
                .setPositionX(model.getPositionX())
                .setPositionY(model.getPositionY())
                .setScaleX(model.getScaleX())
                .setScaleY(model.getScaleY())
                .setRotation(model.getRotation())
                .setDisplayOrder(model.getDisplayOrder());
    }

    @Override
    public StageProp toModel(StagePropViewModel viewModel) {
        StageProp model = stagePersistenceService.getStagePropByUuid(viewModel.getStageId(), viewModel.getUuid())
                .orElseGet(StageProp::new);

        return model
                .setUuid(viewModel.getUuid())
                .setStageId(viewModel.getStageId())
                .setCode(viewModel.getCode())
                .setName(viewModel.getName())
                .setType(viewModel.getType())
                .setLedCount(viewModel.getLedCount())
                .setPositionX(viewModel.getPositionX())
                .setPositionY(viewModel.getPositionY())
                .setScaleX(viewModel.getScaleX())
                .setScaleY(viewModel.getScaleY())
                .setRotation(viewModel.getRotation())
                .setDisplayOrder(viewModel.getDisplayOrder());
    }
}
