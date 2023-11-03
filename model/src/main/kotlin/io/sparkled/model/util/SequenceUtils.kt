package io.sparkled.model.util

import io.sparkled.model.SequenceModel
import io.sparkled.model.SongModel
import io.sparkled.model.constant.ModelConstants.MS_PER_SECOND

/**
 * Helper functions for sequences.
 */
object SequenceUtils {

    /**
     * @param song The song used by the sequence, which contains the duration in milliseconds.
     * @param sequence The sequence, which contains the FPS.
     * @return The number of frames available in the sequence.
     */
    fun getFrameCount(song: SongModel, sequence: SequenceModel): Int {
        return (song.durationMs / MS_PER_SECOND.toFloat() * sequence.framesPerSecond).toInt()
    }
}
