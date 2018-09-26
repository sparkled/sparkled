package io.sparkled.rest;

import io.sparkled.model.entity.Playlist;
import io.sparkled.model.playlist.PlaylistAction;
import io.sparkled.model.playlist.PlaylistActionType;
import io.sparkled.music.PlaylistService;
import io.sparkled.persistence.playlist.PlaylistPersistenceService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/player")
public class PlayerRestService extends RestService {

    private final PlaylistPersistenceService playlistPersistenceService;
    private final PlaylistService playlistService;

    @Inject
    public PlayerRestService(PlaylistPersistenceService playlistPersistenceService,
                             PlaylistService playlistService) {
        this.playlistPersistenceService = playlistPersistenceService;
        this.playlistService = playlistService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSequences() {
        List<Playlist> playlists = playlistPersistenceService.getAllPlaylists();
        return getJsonResponse(playlists);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response adjustPlayback(PlaylistAction action) {
        if (action.getType() == PlaylistActionType.PLAY) {
            Optional<Playlist> playlist = playlistPersistenceService.getPlaylistById(action.getPlaylistId());
            if (!playlist.isPresent()) {
                return getResponse(Response.Status.NOT_FOUND);
            }
            playlistService.play(playlist.get());
        } else if (action.getType() == PlaylistActionType.STOP) {
            playlistService.stopPlayback();
        }

        return getResponse(Response.Status.OK);
    }
}