package io.sparkled.model.util

object MathUtils {

    /**
     * @return The amount of overlap between two ranges. For convenience, this function will work with ranges expressed
     * from start-to-end and from end-to-start.
     */
    fun getOverlap(start1: Float, end1: Float, start2: Float, end2: Float): Float {
        val s1 = Math.min(start1, end1)
        val e1 = Math.max(start1, end1)
        val s2 = Math.min(start2, end2)
        val e2 = Math.max(start2, end2)

        val start = Math.max(s1, s2)
        val end = Math.min(e1, e2)
        return Math.max(0f, end - start)
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
     * @param value the value to be constrained
     * @param min the minimum allowable value (inclusive)
     * @param max the maximum allowable value (inclusive)
     * @return the value, constrained by min and max
     */
    fun constrain(value: Int, min: Int, max: Int): Int {
        return Math.max(min, Math.min(max, value))
    }
}
