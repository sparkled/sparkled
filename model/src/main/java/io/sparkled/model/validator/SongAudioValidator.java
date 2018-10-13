package io.sparkled.model.validator;

import io.sparkled.model.entity.SongAudio;
import io.sparkled.model.validator.exception.EntityValidationException;

public class SongAudioValidator {

    public void validate(SongAudio songAudio) {
        if (songAudio.getSongId() == null) {
            throw new EntityValidationException(Errors.SONG_ID_MISSING);
        }
    }

    private static class Errors {
        static final String SONG_ID_MISSING = "Song audio has no song identifier.";
    }
}
