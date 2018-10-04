package io.sparkled.rest;

import io.sparkled.model.entity.Stage;
import io.sparkled.persistence.stage.StagePersistenceService;
import io.sparkled.rest.response.IdResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/stages")
public class StageRestService extends RestService {

    private final StagePersistenceService stagePersistenceService;

    @Inject
    public StageRestService(StagePersistenceService stagePersistenceService) {
        this.stagePersistenceService = stagePersistenceService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStage(Stage stage) {
        stage.setId(null);
        int stageId = stagePersistenceService.saveStage(stage);
        return getJsonResponse(new IdResponse(stageId));
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStage(@PathParam("id") int id, Stage stage) {
        if (id != stage.getId()) {
            return getJsonResponse(Response.Status.BAD_REQUEST, "Stage ID does not match URL.");
        }

        Integer savedId = stagePersistenceService.saveStage(stage);
        if (savedId == null) {
            return getJsonResponse(Response.Status.NOT_FOUND, "Stage not found.");
        } else {
            IdResponse idResponse = new IdResponse(savedId);
            return getJsonResponse(idResponse);
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStage(@PathParam("id") int id) {
        int stageId = stagePersistenceService.deleteStage(id);
        return getJsonResponse(new IdResponse(stageId));
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
    public Response getStage(@PathParam("id") int id) {
        Optional<Stage> stage = stagePersistenceService.getStageById(id);

        if (stage.isPresent()) {
            return getJsonResponse(stage.get());
        } else {
            return getResponse(Response.Status.NOT_FOUND);
        }
    }
}