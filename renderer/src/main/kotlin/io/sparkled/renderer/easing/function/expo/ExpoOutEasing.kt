package io.sparkled.renderer.easing.function.expo

import io.sparkled.model.animation.easing.Easing
import io.sparkled.renderer.easing.EasingFunction

/**
 * An exponential easing function.
 */
class ExpoOutEasing : EasingFunction() {

    override fun getUnscaledProgress(easing: Easing, currentFrame: Int, frameCount: Int): Float {
        val frameNumber = currentFrame + 1
        return if (frameNumber == frameCount) {
            1f
        } else {
            -Math.pow(2.0, (-10 * frameNumber / frameCount).toDouble()).toFloat() + 1
        }
    }
}
