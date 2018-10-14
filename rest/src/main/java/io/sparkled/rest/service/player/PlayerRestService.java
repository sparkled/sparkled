package io.sparkled.rest.service.player;

import io.sparkled.model.playlist.PlaylistAction;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/player")
public class PlayerRestService {

    private final PlayerRestServiceHandler handler;

    @Inject
    public PlayerRestService(PlayerRestServiceHandler handler) {
        this.handler = handler;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response adjustPlayback(PlaylistAction action) {
        return handler.adjustPlayback(action);
    }
}