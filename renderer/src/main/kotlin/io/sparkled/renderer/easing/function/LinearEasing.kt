package io.sparkled.renderer.easing.function

import io.sparkled.model.animation.easing.Easing
import io.sparkled.renderer.api.SparkledEasing

/**
 * An easing function that interpolates uniformly between 0 and 1.
 */
object LinearEasing : SparkledEasing {

    override val id = "sparkled:linear:1.0.0"
    override val name = "Linear"

    override fun getProgress(easing: Easing, currentFrame: Int, frameCount: Int): Float {
        return currentFrame / (frameCount.toFloat() - 1)
    }
}
