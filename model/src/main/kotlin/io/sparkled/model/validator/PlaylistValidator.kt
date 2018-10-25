package io.sparkled.model.validator

import io.sparkled.model.entity.Playlist
import io.sparkled.model.validator.exception.EntityValidationException

class PlaylistValidator {

    fun validate(playlist: Playlist) {
        if (playlist.getName() == null) {
            throw EntityValidationException(Errors.NAME_MISSING)
        }
    }

    private object Errors {
        internal const val NAME_MISSING = "Playlist name must not be empty."
    }
}
