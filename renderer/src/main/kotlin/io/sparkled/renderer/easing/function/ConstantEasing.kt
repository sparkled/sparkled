package io.sparkled.renderer.easing.function

import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.param.ParamName
import io.sparkled.renderer.easing.EasingFunction
import io.sparkled.renderer.util.ParamUtils

class ConstantEasing : EasingFunction {
    @Override
    fun getProgress(easing: Easing, currentFrame: Float, frameCount: Float): Float {
        return ParamUtils.getDecimalValue(easing, ParamName.PERCENT) / 100f
    }
}
