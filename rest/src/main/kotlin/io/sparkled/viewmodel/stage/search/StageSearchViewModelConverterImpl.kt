package io.sparkled.viewmodel.stage.search

import io.sparkled.model.entity.Stage

import java.util.stream.Collectors.toList

class StageSearchViewModelConverterImpl : StageSearchViewModelConverter() {

    @Override
    fun toViewModels(models: Collection<Stage>): List<StageSearchViewModel> {
        return models.stream().map(???({ this.toViewModel(it) })).collect(toList())
    }

    private fun toViewModel(model: Stage): StageSearchViewModel {
        return StageSearchViewModel()
                .setId(model.getId())
                .setName(model.getName())
    }
}
