package io.sparkled.model.util

import kotlin.math.max
import kotlin.math.min

object MathUtils {

    /**
     * @return The amount of overlap between two ranges. For convenience, this function will work with ranges expressed
     * from start-to-end and from end-to-start.
     */
    fun getOverlap(start1: Float, end1: Float, start2: Float, end2: Float): Float {
        val s1 = min(start1, end1)
        val e1 = max(start1, end1)
        val s2 = min(start2, end2)
        val e2 = max(start2, end2)

        val start = max(s1, s2)
        val end = min(e1, e2)
        return max(0f, end - start)
    }

    /**
     * @param value the number to map
     * @param oldMin the lower bound of the value’s current range
     * @param oldMax the upper bound of the value’s current range
     * @param newMin the lower bound of the value’s target range
     * @param newMax the upper bound of the value’s target range
     * @return a value mapped from a given range to a new range
     */
    fun map(value: Float, oldMin: Float, oldMax: Float, newMin: Float, newMax: Float): Float {
        return (value - oldMin) * (newMax - newMin) / (oldMax - oldMin) + newMin
    }

    /**
     * Linear interpolation between two numbers
     * @param from the start value
     * @param to the end value
     * @param progress a value between 0 and 1, where 0 yields from, and 1 yields to
     */
    fun lerp(from: Float, to: Float, progress: Float): Float {
        return from * (1 - progress) + to * progress
    }
}
