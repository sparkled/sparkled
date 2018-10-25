package io.sparkled.renderer.easing.function

import io.sparkled.model.animation.easing.Easing
import io.sparkled.renderer.easing.EasingFunction

/**
 * An easing function that interpolates uniformly between 0 and 1.
 */
class LinearEasing : EasingFunction {
    @Override
    fun getProgress(easing: Easing, currentFrame: Float, frameCount: Float): Float {
        return currentFrame / (frameCount - 1)
    }
}
