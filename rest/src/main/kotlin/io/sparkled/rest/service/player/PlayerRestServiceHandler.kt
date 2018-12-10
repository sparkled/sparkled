package io.sparkled.rest.service.player

import io.sparkled.model.playlist.PlaylistAction
import io.sparkled.model.playlist.PlaylistActionType
import io.sparkled.music.PlaybackService
import io.sparkled.persistence.playlist.PlaylistPersistenceService
import io.sparkled.rest.service.RestServiceHandler
import javax.inject.Inject
import javax.ws.rs.core.Response

open class PlayerRestServiceHandler
@Inject constructor(
    private val playlistPersistenceService: PlaylistPersistenceService,
    private val playbackService: PlaybackService
) : RestServiceHandler() {

    internal fun adjustPlayback(action: PlaylistAction): Response {
        val type = action.getType()
        if (type == null) {
            return respond(Response.Status.BAD_REQUEST, "A valid playback action must be supplied.")
        } else if (type === PlaylistActionType.PLAY) {
            if (!play(action)) {
                return respond(Response.Status.NOT_FOUND, "Playlist not found.")
            }
        } else if (type === PlaylistActionType.STOP) {
            stop()
        }

        return respondOk()
    }

    private fun play(action: PlaylistAction): Boolean {
        val playlist = playlistPersistenceService.getPlaylistById(action.getPlaylistId()!!)
        playlist?.apply(playbackService::play)
        return playlist != null
    }

    private fun stop() {
        playbackService.stopPlayback()
    }
}