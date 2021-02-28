package io.sparkled.model.util

import io.sparkled.model.constant.ModelConstants.MS_PER_SECOND
import io.sparkled.model.entity.v2.SequenceEntity
import io.sparkled.model.entity.v2.SongEntity

/**
 * Helper functions for sequences.
 */
object SequenceUtils {

    /**
     * @param song The song used by the sequence, which contains the duration in milliseconds.
     * @param sequence The sequence, which contains the FPS.
     * @return The number of frames available in the sequence.
     */
    fun getFrameCount(song: SongEntity, sequence: SequenceEntity): Int {
        return (song.durationMs / MS_PER_SECOND.toFloat() * sequence.framesPerSecond).toInt()
    }
}
