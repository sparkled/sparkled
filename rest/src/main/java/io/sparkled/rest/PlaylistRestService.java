package io.sparkled.rest;

import io.sparkled.music.PlaylistService;
import io.sparkled.persistence.playlist.PlaylistPersistenceService;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path("/playlist")
public class PlaylistRestService extends RestService {

    @Inject
    public PlaylistRestService(PlaylistPersistenceService playlistPersistenceService,
                               PlaylistService playlistService) {
    }
}