package net.chrisparton.sparkled.rest;

import net.chrisparton.sparkled.entity.Stage;
import net.chrisparton.sparkled.persistence.stage.StagePersistenceService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/stage")
public class StageRestService extends RestService {

    private StagePersistenceService persistenceService = new StagePersistenceService();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStage(@PathParam("id") int id) {
        Optional<Stage> stage = persistenceService.getStageById(id);

        if (stage.isPresent()) {
            return getJsonResponse(stage.get());
        }

        return getResponse(Response.Status.NOT_FOUND);
    }
}