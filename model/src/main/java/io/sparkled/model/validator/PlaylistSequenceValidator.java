package io.sparkled.model.validator;

import io.sparkled.model.entity.PlaylistSequence;
import io.sparkled.model.validator.exception.EntityValidationException;

public class PlaylistSequenceValidator {

    public void validate(PlaylistSequence playlistSequence) throws EntityValidationException {
        if (playlistSequence.getUuid() == null) {
            throw new EntityValidationException(Errors.UUID_MISSING);
        } else if (playlistSequence.getPlaylistId() == null) {
            throw new EntityValidationException(Errors.PLAYLIST_ID_MISSING);
        } else if (playlistSequence.getSequenceId() == null) {
            throw new EntityValidationException(Errors.SEQUENCE_ID_MISSING);
        } else if (playlistSequence.getDisplayOrder() == null) {
            throw new EntityValidationException(Errors.DISPLAY_ORDER_MISSING);
        }
    }

    private static class Errors {
        static final String UUID_MISSING = "Playlist sequence has no unique identifier.";
        static final String PLAYLIST_ID_MISSING = "Playlist sequence has no playlist identifier.";
        static final String SEQUENCE_ID_MISSING = "Playlist sequence has no sequence identifier.";
        static final String DISPLAY_ORDER_MISSING = "Playlist sequence has no display order.";
    }
}
