package io.sparkled.rest.service.stage;

import com.google.inject.persist.Transactional;
import io.sparkled.model.entity.Stage;
import io.sparkled.model.entity.StageProp;
import io.sparkled.persistence.stage.StagePersistenceService;
import io.sparkled.rest.response.IdResponse;
import io.sparkled.rest.service.RestServiceHandler;
import io.sparkled.viewmodel.stage.StageViewModel;
import io.sparkled.viewmodel.stage.StageViewModelConverter;
import io.sparkled.viewmodel.stage.prop.StagePropViewModel;
import io.sparkled.viewmodel.stage.prop.StagePropViewModelConverter;
import io.sparkled.viewmodel.stage.search.StageSearchViewModel;
import io.sparkled.viewmodel.stage.search.StageSearchViewModelConverter;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

class StageRestServiceHandler extends RestServiceHandler {

    private final StagePersistenceService stagePersistenceService;
    private final StageViewModelConverter stageViewModelConverter;
    private final StageSearchViewModelConverter stageSearchViewModelConverter;
    private final StagePropViewModelConverter stagePropViewModelConverter;

    @Inject
    public StageRestServiceHandler(StagePersistenceService stagePersistenceService,
                                   StageViewModelConverter stageViewModelConverter,
                                   StageSearchViewModelConverter stageSearchViewModelConverter,
                                   StagePropViewModelConverter stagePropViewModelConverter) {
        this.stagePersistenceService = stagePersistenceService;
        this.stageViewModelConverter = stageViewModelConverter;
        this.stageSearchViewModelConverter = stageSearchViewModelConverter;
        this.stagePropViewModelConverter = stagePropViewModelConverter;
    }

    @Transactional
    Response createStage(StageViewModel stageViewModel) {
        Stage stage = stageViewModelConverter.toModel(stageViewModel);
        stage = stagePersistenceService.createStage(stage);
        return respondOk(new IdResponse(stage.getId()));
    }

    Response getAllStages() {
        List<Stage> stages = stagePersistenceService.getAllStages();
        List<StageSearchViewModel> viewModels = stageSearchViewModelConverter.toViewModels(stages);
        return respondOk(viewModels);
    }

    Response getStage(int stageId) {
        Optional<Stage> stageOptional = stagePersistenceService.getStageById(stageId);

        if (stageOptional.isPresent()) {
            Stage stage = stageOptional.get();
            StageViewModel viewModel = stageViewModelConverter.toViewModel(stage);

            List<StagePropViewModel> stageProps = stagePersistenceService
                    .getStagePropsByStageId(stageId)
                    .stream()
                    .map(stagePropViewModelConverter::toViewModel)
                    .collect(toList());
            viewModel.setStageProps(stageProps);

            return respondOk(viewModel);
        }

        return respond(Response.Status.NOT_FOUND, "Stage not found.");
    }

    @Transactional
    Response updateStage(int id, StageViewModel stageViewModel) {
        stageViewModel.setId(id); // Prevent client-side ID tampering.

        Stage stage = stageViewModelConverter.toModel(stageViewModel);
        List<StageProp> stageProps = stageViewModel.getStageProps()
                .stream()
                .map(stagePropViewModelConverter::toModel)
                .map(ps -> ps.setStageId(id))
                .collect(toList());

        stagePersistenceService.saveStage(stage, stageProps);
        return respondOk();
    }

    @Transactional
    Response deleteStage(int id) {
        stagePersistenceService.deleteStage(id);
        return respondOk();
    }
}