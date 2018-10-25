package io.sparkled.renderer.easing.function

import io.sparkled.model.animation.easing.Easing
import io.sparkled.renderer.easing.EasingFunction

/**
 * An easing function that interpolates uniformly between 0 and 1.
 */
class LinearEasing : EasingFunction {

    override fun getProgress(easing: Easing, currentFrame: Int, frameCount: Int): Float {
        return currentFrame / (frameCount.toFloat() - 1)
    }
}
