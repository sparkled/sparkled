package io.sparkled.rest.service.sequence;

import io.sparkled.viewmodel.sequence.SequenceViewModel;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

@Path("/sequences")
public class SequenceRestService {

    static final String MP3_MIME_TYPE = "audio/mpeg";

    private final SequenceRestServiceHandler handler;

    @Inject
    public SequenceRestService(SequenceRestServiceHandler handler) {
        this.handler = handler;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSequence(@FormDataParam("sequence") String sequenceJson,
                                   @FormDataParam("mp3") InputStream uploadedInputStream,
                                   @FormDataParam("mp3") FormDataContentDisposition fileDetail) throws IOException {
        return handler.createSequence(sequenceJson, uploadedInputStream);
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
    @Path("/{id}/audio")
    @Produces(MP3_MIME_TYPE)
    public Response getSequenceSongAudio(@PathParam("id") int id) {
        return handler.getSequenceSongAudio(id);
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