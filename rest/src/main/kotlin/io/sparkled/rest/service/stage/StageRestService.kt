package io.sparkled.rest.service.stage

import io.sparkled.viewmodel.stage.StageViewModel

import javax.inject.Inject
import javax.ws.rs.*
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

    val allStages: Response
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        get() = handler.getAllStages()

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