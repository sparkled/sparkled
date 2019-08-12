package io.sparkled.rest

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.spring.tx.annotation.Transactional
import io.sparkled.model.playlist.PlaylistAction
import io.sparkled.model.playlist.PlaylistActionType
import io.sparkled.music.PlaybackService
import io.sparkled.persistence.playlist.PlaylistPersistenceService

@Controller("/rest/player")
open class PlayerController(
    private val playbackService: PlaybackService,
    private val playlistPersistenceService: PlaylistPersistenceService
) {

    @Post("/")
    @Transactional(readOnly = true)
    open fun adjustPlayback(action: PlaylistAction): HttpResponse<Any> {
        val type = action.getType()
        if (type == null) {
            return HttpResponse.badRequest("A valid playback action must be supplied.")
        } else if (type === PlaylistActionType.PLAY) {
            if (!play(action)) {
                return HttpResponse.notFound("Playlist not found.")
            }
        } else if (type === PlaylistActionType.STOP) {
            stop()
        }

        return HttpResponse.ok()
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
