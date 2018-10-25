package io.sparkled.model.validator

import io.sparkled.model.entity.SongAudio
import io.sparkled.model.validator.exception.EntityValidationException

class SongAudioValidator {

    fun validate(songAudio: SongAudio) {
        if (songAudio.getSongId() == null) {
            throw EntityValidationException(Errors.SONG_ID_MISSING)
        }
    }

    private object Errors {
        internal const val SONG_ID_MISSING = "Song audio has no song identifier."
    }
}
