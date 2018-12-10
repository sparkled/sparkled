package io.sparkled.rest.service.player

import io.sparkled.model.playlist.PlaylistAction
import javax.inject.Inject
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/player")
class PlayerRestService
@Inject constructor(private val handler: PlayerRestServiceHandler) {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun adjustPlayback(action: PlaylistAction): Response {
        return handler.adjustPlayback(action)
    }
}