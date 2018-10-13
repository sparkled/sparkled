package io.sparkled.model.util;

import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.Song;

import static io.sparkled.model.constant.ModelConstants.MS_PER_SECOND;

/**
 * Helper functions for sequences.
 */
public class SequenceUtils {

    private SequenceUtils() {
    }

    /**
     *
     * @param song The song used by the sequence, which contains the duration in milliseconds.
     * @param sequence The sequence, which contains the FPS.
     * @return The number of frames available in the sequence.
     */
    public static int getFrameCount(Song song, Sequence sequence) {
        return (song.getDurationMs() / MS_PER_SECOND) * sequence.getFramesPerSecond();
    }
}
