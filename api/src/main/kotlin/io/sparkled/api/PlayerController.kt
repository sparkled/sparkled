package io.sparkled.api

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.sparkled.model.playlist.PlaylistAction
import io.sparkled.model.playlist.PlaylistActionType
import io.sparkled.music.PlaybackService
import io.sparkled.persistence.DbService
import io.sparkled.persistence.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@ExecuteOn(TaskExecutors.IO)
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/player")
class PlayerController(
    private val playbackService: PlaybackService,
    private val db: DbService
) {

    @Post("/")
    @Transactional
    fun adjustPlayback(@Body action: PlaylistAction): HttpResponse<Any> {
        return when (action.action) {
            PlaylistActionType.PLAY_PLAYLIST, PlaylistActionType.PLAY_SEQUENCE -> {
                play(action)
                HttpResponse.ok()
            }

            PlaylistActionType.STOP -> {
                stop()
                HttpResponse.ok()
            }

            else -> HttpResponse.badRequest("A valid playback action must be supplied.")
        }
    }

    private fun play(action: PlaylistAction) {
        val sequences = if (action.action === PlaylistActionType.PLAY_PLAYLIST) {
            db.sequences.findAllByPlaylistId(action.playlistId ?: "")
        } else {
            val sequence = db.sequences.findByIdOrNull(action.sequenceId ?: "")
            listOfNotNull(sequence)
        }

        playbackService.play(sequences, action.repeat ?: true)
    }

    private fun stop() {
        playbackService.stopPlayback()
    }
}
