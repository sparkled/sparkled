package io.sparkled.model.validator;

import io.sparkled.model.entity.Playlist;
import io.sparkled.model.validator.exception.EntityValidationException;

public class PlaylistValidator {

    public void validate(Playlist playlist) {
        if (playlist.getName() == null) {
            throw new EntityValidationException(Errors.NAME_MISSING);
        }
    }

    private static class Errors {
        static final String NAME_MISSING = "Playlist name must not be empty.";
    }
}
