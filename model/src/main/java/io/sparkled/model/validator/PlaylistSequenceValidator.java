package io.sparkled.model.validator;

import io.sparkled.model.entity.PlaylistSequence;
import io.sparkled.model.validator.exception.EntityValidationException;

public class PlaylistSequenceValidator {

    public void validate(PlaylistSequence playlistSequence) throws EntityValidationException {
        if (playlistSequence.getUuid() == null) {
            throw new EntityValidationException("Playlist sequence has no unique identifier.");
        } else if (playlistSequence.getPlaylistId() == null) {
            throw new EntityValidationException("Playlist sequence has no playlist identifier.");
        } else if (playlistSequence.getSequenceId() == null) {
            throw new EntityValidationException("Playlist sequence has no sequence identifier.");
        } else if (playlistSequence.getDisplayOrder() == null) {
            throw new EntityValidationException("Playlist sequence has no display order.");
        }
    }
}
