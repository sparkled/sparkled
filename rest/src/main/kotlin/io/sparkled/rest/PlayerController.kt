package io.sparkled.rest

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.sparkled.model.entity.v2.SequenceEntity
import io.sparkled.model.playlist.PlaylistAction
import io.sparkled.model.playlist.PlaylistActionType
import io.sparkled.music.PlaybackService
import io.sparkled.persistence.DbService
import io.sparkled.persistence.getById
import io.sparkled.persistence.query.sequence.GetSequencesByPlaylistIdQuery
import org.springframework.transaction.annotation.Transactional

@Controller("/api/player")
open class PlayerController(
    private val playbackService: PlaybackService,
    private val db: DbService
) {

    @Post("/")
    @Transactional(readOnly = true)
    open fun adjustPlayback(@Body action: PlaylistAction): HttpResponse<Any> {
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
            db.query(GetSequencesByPlaylistIdQuery(action.playlistId ?: -1))
        } else {
            val sequence = db.getById<SequenceEntity>(action.sequenceId ?: -1)
            listOfNotNull(sequence)
        }

        playbackService.play(sequences, action.repeat ?: true)
    }

    private fun stop() {
        playbackService.stopPlayback()
    }
}
