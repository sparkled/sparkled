package io.sparkled.viewmodel.stage.search

import io.sparkled.model.entity.Stage

class StageSearchViewModelConverterImpl : StageSearchViewModelConverter() {

    override fun toViewModels(models: Collection<Stage>): List<StageSearchViewModel> {
        return models.map(this::toViewModel).toList()
    }

    private fun toViewModel(model: Stage): StageSearchViewModel {
        return StageSearchViewModel()
            .setId(model.getId()!!)
            .setName(model.getName()!!)
    }
}
