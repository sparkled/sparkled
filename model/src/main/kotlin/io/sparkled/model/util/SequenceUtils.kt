package io.sparkled.model.util

import io.sparkled.model.constant.ModelConstants.Companion.MS_PER_SECOND
import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.Song

/**
 * Helper functions for sequences.
 */
object SequenceUtils {

    /**
     * @param song The song used by the sequence, which contains the duration in milliseconds.
     * @param sequence The sequence, which contains the FPS.
     * @return The number of frames available in the sequence.
     */
    fun getFrameCount(song: Song, sequence: Sequence): Int {
        return song.getDurationMs()!! / MS_PER_SECOND * sequence.getFramesPerSecond()!!
    }
}
