package io.sparkled.renderer.easing.function

import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.param.ParamCode
import io.sparkled.renderer.easing.EasingFunction
import io.sparkled.renderer.util.ParamUtils

/**
 * An easing function that interpolates uniformly between 0 and 1.
 */
class LinearEasing : EasingFunction {

    override fun getProgress(easing: Easing, currentFrame: Int, frameCount: Int): Float {
        val from = ParamUtils.getDecimalValue(easing, ParamCode.PERCENT_FROM, 0f) / 100f
        val to = ParamUtils.getDecimalValue(easing, ParamCode.PERCENT_TO, 100f) / 100f

        return from + (to - from) * (currentFrame / (frameCount.toFloat() - 1))
    }
}
