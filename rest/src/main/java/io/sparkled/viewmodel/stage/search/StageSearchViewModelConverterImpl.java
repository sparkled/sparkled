package io.sparkled.viewmodel.stage.search;

import io.sparkled.model.entity.Stage;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class StageSearchViewModelConverterImpl extends StageSearchViewModelConverter {

    @Override
    public List<StageSearchViewModel> toViewModels(Collection<Stage> models) {
        return models.stream().map(this::toViewModel).collect(toList());
    }

    private StageSearchViewModel toViewModel(Stage model) {
        return new StageSearchViewModel()
                .setId(model.getId())
                .setName(model.getName());
    }
}
