package io.sparkled.model.validator

import io.sparkled.model.entity.Song
import io.sparkled.model.validator.exception.EntityValidationException

class SongValidator {

    fun validate(song: Song) {
        if (song.getName() == null) {
            throw EntityValidationException(Errors.NAME_MISSING)
        }
    }

    private object Errors {
        internal val NAME_MISSING = "Song name must not be empty."
    }
}
