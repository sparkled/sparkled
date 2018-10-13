package io.sparkled.rest.service.sequence;

import io.sparkled.viewmodel.sequence.SequenceViewModel;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/sequences")
public class SequenceRestService {

    private final SequenceRestServiceHandler handler;

    @Inject
    public SequenceRestService(SequenceRestServiceHandler handler) {
        this.handler = handler;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSequence(SequenceViewModel sequenceViewModel) {
        return handler.createSequence(sequenceViewModel);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSequences() {
        return handler.getAllSequences();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSequence(@PathParam("id") int sequenceId) {
        return handler.getSequence(sequenceId);
    }

    @GET
    @Path("/{id}/stage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSequenceStage(@PathParam("id") int sequenceId) {
        return handler.getSequenceStage(sequenceId);
    }

    @GET
    @Path("/{id}/songAudio")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSequenceSongAudio(@PathParam("id") int sequenceId) {
        return handler.getSequenceSongAudio(sequenceId);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSequence(@PathParam("id") int id, SequenceViewModel sequenceViewModel) {
        return handler.updateSequence(id, sequenceViewModel);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSequence(@PathParam("id") int id) {
        return handler.deleteSequence(id);
    }
}