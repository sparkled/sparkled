package io.sparkled.viewmodel.stage.prop

import io.sparkled.model.entity.StageProp
import io.sparkled.persistence.stage.StagePersistenceService

import javax.inject.Inject

class StagePropViewModelConverterImpl @Inject
constructor(private val stagePersistenceService: StagePersistenceService) : StagePropViewModelConverter() {

    @Override
    fun toViewModel(model: StageProp): StagePropViewModel {
        return StagePropViewModel()
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
                .setDisplayOrder(model.getDisplayOrder())
    }

    @Override
    fun toModel(viewModel: StagePropViewModel): StageProp {
        val model = stagePersistenceService.getStagePropByUuid(viewModel.getStageId(), viewModel.getUuid())
                .orElseGet(???({ StageProp() }))

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
                .setDisplayOrder(viewModel.getDisplayOrder())
    }
}
