package io.sparkled.viewmodel.stage.search;

import io.sparkled.model.entity.Stage;

public class StageSearchViewModelConverterImpl extends StageSearchViewModelConverter {

    @Override
    public StageSearchViewModel toViewModel(Stage model) {
        return new StageSearchViewModel()
                .setId(model.getId())
                .setName(model.getName());
    }
}
