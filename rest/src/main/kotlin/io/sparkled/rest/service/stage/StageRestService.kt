package io.sparkled.rest.service.stage;

import io.sparkled.viewmodel.stage.StageViewModel;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/stages")
public class StageRestService {

    private final StageRestServiceHandler handler;

    @Inject
    public StageRestService(StageRestServiceHandler handler) {
        this.handler = handler;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStage(StageViewModel stageViewModel) {
        return handler.createStage(stageViewModel);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllStages() {
        return handler.getAllStages();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStage(@PathParam("id") int stageId) {
        return handler.getStage(stageId);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStage(@PathParam("id") int id, StageViewModel stageViewModel) {
        return handler.updateStage(id, stageViewModel);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStage(@PathParam("id") int id) {
        return handler.deleteStage(id);
    }
}