package io.sparkled.rest.service.player;

import io.sparkled.model.entity.Playlist;
import io.sparkled.model.playlist.PlaylistAction;
import io.sparkled.model.playlist.PlaylistActionType;
import io.sparkled.music.PlaybackService;
import io.sparkled.persistence.playlist.PlaylistPersistenceService;
import io.sparkled.rest.service.RestServiceHandler;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.Optional;

public class PlayerRestServiceHandler extends RestServiceHandler {

    private final PlaylistPersistenceService playlistPersistenceService;
    private final PlaybackService playbackService;

    @Inject
    public PlayerRestServiceHandler(PlaylistPersistenceService playlistPersistenceService,
                                    PlaybackService playbackService) {
        this.playlistPersistenceService = playlistPersistenceService;
        this.playbackService = playbackService;
    }

    Response adjustPlayback(PlaylistAction action) {
        PlaylistActionType type = action.getType();
        if (type == null) {
            return respond(Response.Status.BAD_REQUEST, "A valid playback action must be supplied.");
        } else if (type == PlaylistActionType.PLAY) {
            if (!play(action)) {
                return respond(Response.Status.NOT_FOUND, "Playlist not found.");
            }
        } else if (type == PlaylistActionType.STOP) {
            stop();
        }

        return respondOk();
    }

    private boolean play(PlaylistAction action) {
        Optional<Playlist> playlist = playlistPersistenceService.getPlaylistById(action.getPlaylistId());
        playlist.ifPresent(playbackService::play);
        return playlist.isPresent();
    }

    private void stop() {
        playbackService.stopPlayback();
    }
}