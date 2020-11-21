package io.sparkled.renderer.api

import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.util.MathUtils.map

interface SparkledEasing : SparkledPlugin {

    fun getScaledProgress(easing: Easing, currentFrame: Int, frameCount: Int): Float {
        val progress = getProgress(easing, currentFrame, frameCount)
        return map(progress, 0f, 1f, easing.start / 100f, easing.end / 100f)
    }

    /**
     * @param easing The easing configuration, including parameters
     * @param currentFrame The current animation effect frame being rendered
     * @param frameCount The number of frames in the animation effect (implementing functions will likely subtract 1
     *                       from this value, as currentFrame is zero-based)
     *
     * @return A value between 0 and 1 describing the overall progress of the animation effect
     */
    fun getProgress(easing: Easing, currentFrame: Int, frameCount: Int): Float
}
