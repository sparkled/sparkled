package io.sparkled.rest.service.scheduledjob

import io.sparkled.viewmodel.scheduledjob.ScheduledJobViewModel
import javax.inject.Inject
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/scheduledJobs")
class ScheduledJobRestService @Inject
constructor(private val handler: ScheduledJobRestServiceHandler) {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createScheduledJob(playlistViewModel: ScheduledJobViewModel): Response {
        return handler.createScheduledJob(playlistViewModel)
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllScheduledJobs(): Response {
        return handler.getAllScheduledJobs()
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getScheduledJob(@PathParam("id") scheduledJobId: Int): Response {
        return handler.getScheduledJob(scheduledJobId)
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun deleteScheduledJob(@PathParam("id") id: Int): Response {
        return handler.deleteScheduledJob(id)
    }
}