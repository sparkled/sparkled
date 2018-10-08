package io.sparkled.model.validator;

import io.sparkled.model.entity.Playlist;
import io.sparkled.model.validator.exception.EntityValidationException;

public class PlaylistValidator {

    private Playlist playlist;

    public PlaylistValidator(Playlist playlist) {
        this.playlist = playlist;
    }

    public void validate() {
        if (playlist.getName() == null) {
            throw new EntityValidationException(Errors.NO_NAME);
        }
    }

    private static class Errors {
        static final String NO_NAME = "Playlist name must not be empty.";
    }
}
