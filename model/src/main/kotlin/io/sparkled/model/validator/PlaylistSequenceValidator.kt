package io.sparkled.model.validator

import io.sparkled.model.entity.PlaylistSequence
import io.sparkled.model.validator.exception.EntityValidationException

class PlaylistSequenceValidator {

    @Throws(EntityValidationException::class)
    fun validate(playlistSequence: PlaylistSequence) {
        when {
            playlistSequence.getUuid() == null -> throw EntityValidationException(Errors.UUID_MISSING)
            playlistSequence.getPlaylistId() == null -> throw EntityValidationException(Errors.PLAYLIST_ID_MISSING)
            playlistSequence.getSequenceId() == null -> throw EntityValidationException(Errors.SEQUENCE_ID_MISSING)
            playlistSequence.getDisplayOrder() == null -> throw EntityValidationException(Errors.DISPLAY_ORDER_MISSING)
        }
    }

    private object Errors {
        internal const val UUID_MISSING = "Playlist sequence has no unique identifier."
        internal const val PLAYLIST_ID_MISSING = "Playlist sequence has no playlist identifier."
        internal const val SEQUENCE_ID_MISSING = "Playlist sequence has no sequence identifier."
        internal const val DISPLAY_ORDER_MISSING = "Playlist sequence has no display order."
    }
}
