package io.sparkled.model.validator;

import io.sparkled.model.entity.Song;
import io.sparkled.model.validator.exception.EntityValidationException;

public class SongValidator {

    public void validate(Song song) {
        if (song.getName() == null) {
            throw new EntityValidationException(Errors.NAME_MISSING);
        }
    }

    private static class Errors {
        static final String NAME_MISSING = "Song name must not be empty.";
    }
}
