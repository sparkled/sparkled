package io.sparkled.rest.service.stage

import io.sparkled.viewmodel.stage.StageViewModel
import javax.inject.Inject
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/stages")
class StageRestService @Inject
constructor(private val handler: StageRestServiceHandler) {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createStage(stageViewModel: StageViewModel): Response {
        return handler.createStage(stageViewModel)
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun allStages(): Response {
        return handler.getAllStages()
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getStage(@PathParam("id") stageId: Int): Response {
        return handler.getStage(stageId)
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun updateStage(@PathParam("id") id: Int, stageViewModel: StageViewModel): Response {
        return handler.updateStage(id, stageViewModel)
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun deleteStage(@PathParam("id") id: Int): Response {
        return handler.deleteStage(id)
    }
}