package io.sparkled.model.validator

import io.sparkled.model.entity.PlaylistSequence
import io.sparkled.model.validator.exception.EntityValidationException

class PlaylistSequenceValidator {

    @Throws(EntityValidationException::class)
    fun validate(playlistSequence: PlaylistSequence) {
        if (playlistSequence.getUuid() == null) {
            throw EntityValidationException(Errors.UUID_MISSING)
        } else if (playlistSequence.getPlaylistId() == null) {
            throw EntityValidationException(Errors.PLAYLIST_ID_MISSING)
        } else if (playlistSequence.getSequenceId() == null) {
            throw EntityValidationException(Errors.SEQUENCE_ID_MISSING)
        } else if (playlistSequence.getDisplayOrder() == null) {
            throw EntityValidationException(Errors.DISPLAY_ORDER_MISSING)
        }
    }

    private object Errors {
        internal val UUID_MISSING = "Playlist sequence has no unique identifier."
        internal val PLAYLIST_ID_MISSING = "Playlist sequence has no playlist identifier."
        internal val SEQUENCE_ID_MISSING = "Playlist sequence has no sequence identifier."
        internal val DISPLAY_ORDER_MISSING = "Playlist sequence has no display order."
    }
}
