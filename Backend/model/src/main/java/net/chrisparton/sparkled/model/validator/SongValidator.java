package net.chrisparton.sparkled.model.validator;

import net.chrisparton.sparkled.model.entity.Song;
import net.chrisparton.sparkled.model.validator.exception.EntityValidationException;

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
