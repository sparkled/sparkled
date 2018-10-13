package io.sparkled.rest.service.player;

import io.sparkled.model.entity.Playlist;
import io.sparkled.model.playlist.PlaylistAction;
import io.sparkled.model.playlist.PlaylistActionType;
import io.sparkled.music.PlaylistService;
import io.sparkled.persistence.playlist.PlaylistPersistenceService;
import io.sparkled.rest.service.RestServiceHandler;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

public class PlayerRestServiceHandler extends RestServiceHandler {

    private final PlaylistPersistenceService playlistPersistenceService;
    private final PlaylistService playlistService;

    @Inject
    public PlayerRestServiceHandler(PlaylistPersistenceService playlistPersistenceService,
                                    PlaylistService playlistService) {
        this.playlistPersistenceService = playlistPersistenceService;
        this.playlistService = playlistService;
    }

    Response getAllSequences() {
        List<Playlist> playlists = playlistPersistenceService.getAllPlaylists();
        return getJsonResponse(playlists);
    }

    Response adjustPlayback(PlaylistAction action) {
        PlaylistActionType type = action.getType();
        if (type == null) {
            return getJsonResponse(Response.Status.BAD_REQUEST, "A valid playback action must be supplied.");
        } else if (type == PlaylistActionType.PLAY) {
            Optional<Playlist> playlist = playlistPersistenceService.getPlaylistById(action.getPlaylistId());
            if (!playlist.isPresent()) {
                return getResponse(Response.Status.NOT_FOUND);
            }
            playlistService.play(playlist.get());
        } else if (type == PlaylistActionType.STOP) {
            playlistService.stopPlayback();
        }

        return getResponse(Response.Status.OK);
    }
}