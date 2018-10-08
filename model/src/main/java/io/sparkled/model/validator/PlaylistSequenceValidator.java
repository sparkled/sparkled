package io.sparkled.model.validator;

import io.sparkled.model.entity.PlaylistSequence;
import io.sparkled.model.validator.exception.EntityValidationException;

public class PlaylistSequenceValidator {

    public void validate(PlaylistSequence playlistSequence) throws EntityValidationException {
        if (playlistSequence.getUuid() == null) {
            throw new EntityValidationException(Errors.NO_UUID);
        } else if (playlistSequence.getPlaylistId() == null) {
            throw new EntityValidationException(Errors.NO_PLAYLIST_ID);
        } else if (playlistSequence.getSequenceId() == null) {
            throw new EntityValidationException(Errors.NO_SEQUENCE_ID);
        } else if (playlistSequence.getDisplayOrder() == null) {
            throw new EntityValidationException(Errors.NO_DISPLAY_ORDER);
        }
    }

    private static class Errors {
        static final String NO_UUID = "Playlist sequence has no unique identifier.";
        static final String NO_PLAYLIST_ID = "Playlist sequence has no playlist identifier.";
        static final String NO_SEQUENCE_ID = "Playlist sequence has no sequence identifier.";
        static final String NO_DISPLAY_ORDER = "Playlist sequence has no display order.";
    }
}
