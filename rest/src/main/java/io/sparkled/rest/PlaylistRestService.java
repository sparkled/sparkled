package io.sparkled.rest;

import io.sparkled.model.entity.Playlist;
import io.sparkled.model.playlist.PlaylistAction;
import io.sparkled.persistence.playlist.PlaylistPersistenceService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/playlists")
public class PlaylistRestService extends RestService {

    private final PlaylistPersistenceService playlistPersistenceService;

    @Inject
    public PlaylistRestService(PlaylistPersistenceService playlistPersistenceService) {
        this.playlistPersistenceService = playlistPersistenceService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSequences() {
        List<Playlist> playlists = playlistPersistenceService.getAllPlaylists();
        return getJsonResponse(playlists);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response adjustPlaylist(PlaylistAction action) {
        List<Playlist> playlists = playlistPersistenceService.getAllPlaylists();
        return getJsonResponse(playlists);
    }
}