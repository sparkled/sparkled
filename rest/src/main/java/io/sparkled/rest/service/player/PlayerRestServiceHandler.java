package io.sparkled.rest.service.player;

import io.sparkled.model.entity.Playlist;
import io.sparkled.model.playlist.PlaylistAction;
import io.sparkled.model.playlist.PlaylistActionType;
import io.sparkled.music.MusicPlayerService;
import io.sparkled.persistence.playlist.PlaylistPersistenceService;
import io.sparkled.rest.service.RestServiceHandler;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.Optional;

public class PlayerRestServiceHandler extends RestServiceHandler {

    private final PlaylistPersistenceService playlistPersistenceService;
    private final MusicPlayerService musicPlayerService;

    @Inject
    public PlayerRestServiceHandler(PlaylistPersistenceService playlistPersistenceService,
                                    MusicPlayerService musicPlayerService) {
        this.playlistPersistenceService = playlistPersistenceService;
        this.musicPlayerService = musicPlayerService;
    }

    Response adjustPlayback(PlaylistAction action) {
        PlaylistActionType type = action.getType();
        if (type == null) {
            return respond(Response.Status.BAD_REQUEST, "A valid playback action must be supplied.");
        } else if (type == PlaylistActionType.PLAY) {
            Optional<Playlist> playlist = playlistPersistenceService.getPlaylistById(action.getPlaylistId());
            if (!playlist.isPresent()) {
                return respond(Response.Status.NOT_FOUND, "Playlist not found.");
            }
            musicPlayerService.play(playlist.get());
        } else if (type == PlaylistActionType.STOP) {
            musicPlayerService.stopPlayback();
        }

        return respondOk();
    }
}