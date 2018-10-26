package io.sparkled.viewmodel.stage

import io.sparkled.model.entity.Stage
import io.sparkled.persistence.stage.StagePersistenceService
import io.sparkled.viewmodel.exception.ViewModelConversionException

import javax.inject.Inject

class StageViewModelConverterImpl @Inject
constructor(private val stagePersistenceService: StagePersistenceService) : StageViewModelConverter() {

    override fun toViewModel(model: Stage): StageViewModel {
        return StageViewModel()
                .setId(model.getId())
                .setName(model.getName())
                .setWidth(model.getWidth())
                .setHeight(model.getHeight())
    }

    override fun toModel(viewModel: StageViewModel): Stage {
        val stageId = viewModel.getId()
        val model = getStage(stageId)

        return model
                .setName(viewModel.getName())
                .setWidth(viewModel.getWidth())
                .setHeight(viewModel.getHeight())
    }

    private fun getStage(stageId: Int?): Stage {
        if (stageId == null) {
            return Stage()
        }

        return stagePersistenceService.getStageById(stageId)
                .orElseThrow { ViewModelConversionException("Stage with ID of '$stageId' not found.") }
    }
}
