package io.sparkled.rest

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.spring.tx.annotation.Transactional
import io.sparkled.model.playlist.PlaylistAction
import io.sparkled.model.playlist.PlaylistActionType
import io.sparkled.music.PlaybackService
import io.sparkled.persistence.playlist.PlaylistPersistenceService
import io.sparkled.persistence.sequence.SequencePersistenceService

@Controller("/api/player")
open class PlayerController(
    private val playbackService: PlaybackService,
    private val playlistPersistenceService: PlaylistPersistenceService,
    private val sequencePersistenceService: SequencePersistenceService
) {

    @Post("/")
    @Transactional(readOnly = true)
    open fun adjustPlayback(action: PlaylistAction): HttpResponse<Any> {
        return when (action.type) {
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
        val sequences = if (action.type === PlaylistActionType.PLAY_PLAYLIST) {
            playlistPersistenceService.getSequencesByPlaylistId(action.playlistId ?: -1)
        } else {
            val sequence = sequencePersistenceService.getSequenceById(action.sequenceId ?: -1)
            if (sequence === null) emptyList() else listOf(sequence)
        }

        playbackService.play(sequences)
    }

    private fun stop() {
        playbackService.stopPlayback()
    }
}
