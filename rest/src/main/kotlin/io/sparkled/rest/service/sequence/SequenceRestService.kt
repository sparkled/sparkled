package io.sparkled.rest.service.sequence

import io.sparkled.viewmodel.sequence.SequenceViewModel

import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/sequences")
class SequenceRestService @Inject
constructor(private val handler: SequenceRestServiceHandler) {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createSequence(sequenceViewModel: SequenceViewModel): Response {
        return handler.createSequence(sequenceViewModel)
    }

    val allSequences: Response
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        get() = handler.getAllSequences()

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getSequence(@PathParam("id") sequenceId: Int): Response {
        return handler.getSequence(sequenceId)
    }

    @GET
    @Path("/{id}/stage")
    @Produces(MediaType.APPLICATION_JSON)
    fun getSequenceStage(@PathParam("id") sequenceId: Int): Response {
        return handler.getSequenceStage(sequenceId)
    }

    @GET
    @Path("/{id}/songAudio")
    @Produces(MediaType.APPLICATION_JSON)
    fun getSequenceSongAudio(@PathParam("id") sequenceId: Int): Response {
        return handler.getSequenceSongAudio(sequenceId)
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun updateSequence(@PathParam("id") id: Int, sequenceViewModel: SequenceViewModel): Response {
        return handler.updateSequence(id, sequenceViewModel)
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun deleteSequence(@PathParam("id") id: Int): Response {
        return handler.deleteSequence(id)
    }
}