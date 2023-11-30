package io.sparkled.renderer.easing.function

import io.sparkled.model.animation.easing.Easing
import io.sparkled.renderer.api.SparkledEasing
import kotlin.math.pow

/**
 * An exponential easing function.
 */
object ExpoOutEasing : SparkledEasing {

    override val id = "sparkled:expo-out:1.0.0"
    override val name = "Expo Out"

    override fun getProgress(easing: Easing, currentFrame: Int, frameCount: Int): Float {
        val frameNumber = currentFrame + 1
        return if (frameNumber == frameCount) {
            1f
        } else {
            -2.0.pow((-10 * frameNumber / frameCount).toDouble()).toFloat() + 1
        }
    }
}
