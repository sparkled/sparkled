package io.sparkled.rest.service.playlist;

import io.sparkled.viewmodel.playlist.PlaylistViewModel;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/playlists")
public class PlaylistRestService {

    private final PlaylistRestServiceHandler handler;

    @Inject
    public PlaylistRestService(PlaylistRestServiceHandler handler) {
        this.handler = handler;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPlaylist(PlaylistViewModel playlistViewModel) {
        return handler.createPlaylist(playlistViewModel);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPlaylists() {
        return handler.getAllPlaylists();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaylist(@PathParam("id") int playlistId) {
        return handler.getPlaylist(playlistId);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePlaylist(@PathParam("id") int id, PlaylistViewModel playlistViewModel) {
        return handler.updatePlaylist(id, playlistViewModel);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePlaylist(@PathParam("id") int id) {
        return handler.deletePlaylist(id);
    }
}