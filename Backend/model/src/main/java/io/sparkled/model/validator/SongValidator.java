package io.sparkled.model.validator;

import io.sparkled.model.entity.Song;
import io.sparkled.model.validator.exception.EntityValidationException;

public class SongValidator {

    private Song song;

    public SongValidator(Song song) {
        this.song = song;
    }

    public void validate() {
        if (song.getName() == null) {
            throw new EntityValidationException("Song name must not be empty.");
        }
    }
}
