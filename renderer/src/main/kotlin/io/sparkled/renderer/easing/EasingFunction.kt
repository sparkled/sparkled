package io.sparkled.renderer.easing

import io.sparkled.model.animation.easing.Easing

interface EasingFunction {

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
