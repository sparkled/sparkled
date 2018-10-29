package io.sparkled.renderer.util

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
}
