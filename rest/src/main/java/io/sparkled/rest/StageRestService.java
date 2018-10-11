package io.sparkled.rest;

import io.sparkled.model.entity.Stage;
import io.sparkled.model.entity.StageProp;
import io.sparkled.persistence.stage.StagePersistenceService;
import io.sparkled.rest.response.IdResponse;
import io.sparkled.viewmodel.stage.StageViewModel;
import io.sparkled.viewmodel.stage.StageViewModelConverter;
import io.sparkled.viewmodel.stage.prop.StagePropViewModel;
import io.sparkled.viewmodel.stage.prop.StagePropViewModelConverter;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/stages")
public class StageRestService extends RestService {

    private final StagePersistenceService stagePersistenceService;
    private final StageViewModelConverter stageViewModelConverter;
    private final StagePropViewModelConverter stagePropViewModelConverter;

    @Inject
    public StageRestService(StagePersistenceService stagePersistenceService,
                            StageViewModelConverter stageViewModelConverter,
                            StagePropViewModelConverter stagePropViewModelConverter) {
        this.stagePersistenceService = stagePersistenceService;
        this.stageViewModelConverter = stageViewModelConverter;
        this.stagePropViewModelConverter = stagePropViewModelConverter;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStage(StageViewModel stageViewModel) {
        Stage stage = stageViewModelConverter.toModel(stageViewModel);
        stage = stagePersistenceService.createStage(stage);
        return getJsonResponse(new IdResponse(stage.getId()));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllStages() {
        List<Stage> stages = stagePersistenceService.getAllStages();
        return getJsonResponse(stages);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStage(@PathParam("id") int stageId) {
        Optional<Stage> stageOptional = stagePersistenceService.getStageById(stageId);

        if (stageOptional.isPresent()) {
            Stage stage = stageOptional.get();
            StageViewModel viewModel = stageViewModelConverter.toViewModel(stage);

            List<StagePropViewModel> stageProps = stagePersistenceService
                    .getStagePropsByStageId(stageId)
                    .stream()
                    .map(stagePropViewModelConverter::toViewModel)
                    .collect(Collectors.toList());
            viewModel.setStageProps(stageProps);

            return getJsonResponse(viewModel);
        }

        return getJsonResponse(Response.Status.NOT_FOUND);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStage(@PathParam("id") int id, StageViewModel stageViewModel) {
        stageViewModel.setId(id); // Prevent client-side ID tampering.

        Stage stage = stageViewModelConverter.toModel(stageViewModel);
        List<StageProp> stageProps = stageViewModel.getStageProps()
                .stream()
                .map(stagePropViewModelConverter::toModel)
                .map(ps -> ps.setStageId(id))
                .collect(Collectors.toList());

        stagePersistenceService.saveStage(stage, stageProps);
        return getResponse(Response.Status.OK);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStage(@PathParam("id") int id) {
        stagePersistenceService.deleteStage(id);
        return getResponse(Response.Status.OK);
    }
}