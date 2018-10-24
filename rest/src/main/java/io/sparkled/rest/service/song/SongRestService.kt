package io.sparkled.rest.service.song;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

@Path("/songs")
public class SongRestService {

    private final SongRestServiceHandler handler;

    @Inject
    public SongRestService(SongRestServiceHandler handler) {
        this.handler = handler;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSong(@FormDataParam("song") String songViewModelJson,
                               @FormDataParam("mp3") InputStream inputStream,
                               @FormDataParam("mp3") FormDataContentDisposition fileDetail) throws IOException {
        return handler.createSong(songViewModelJson, inputStream);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSongs() {
        return handler.getAllSongs();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSong(@PathParam("id") int songId) {
        return handler.getSong(songId);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSong(@PathParam("id") int id) {
        return handler.deleteSong(id);
    }
}