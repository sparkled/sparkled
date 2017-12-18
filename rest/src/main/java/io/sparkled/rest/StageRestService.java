package io.sparkled.rest;

import io.sparkled.model.entity.Stage;
import io.sparkled.persistence.stage.StagePersistenceService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/stages")
public class StageRestService extends RestService {

    private final StagePersistenceService stagePersistenceService;

    @Inject
    public StageRestService(StagePersistenceService stagePersistenceService) {
        this.stagePersistenceService = stagePersistenceService;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStage(@PathParam("id") int id) {
        Optional<Stage> stage = stagePersistenceService.getStageById(id);

        if (stage.isPresent()) {
            return getJsonResponse(stage.get());
        }

        return getResponse(Response.Status.NOT_FOUND);
    }
}