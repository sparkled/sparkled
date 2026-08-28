package io.sparkled.model.util

import io.sparkled.model.SongModel
import io.sparkled.model.constant.ModelConstants.DEFAULT_FRAMES_PER_SECOND
import io.sparkled.model.constant.ModelConstants.MS_PER_SECOND

/**
 * Helper functions for sequences.
 */
object SequenceUtils {

    /**
     * @param song The song used by the sequence, which contains the duration in milliseconds.
     * @return The number of frames available in the sequence.
     */
    fun getFrameCount(song: SongModel): Int {
        return (song.durationMs / MS_PER_SECOND.toFloat() * DEFAULT_FRAMES_PER_SECOND).toInt()
    }
}
