package io.sparkled.renderer.easing

import io.sparkled.model.animation.easing.EasingTypeCode
import io.sparkled.renderer.easing.function.ConstantEasing
import io.sparkled.renderer.easing.function.LinearEasing
import java.util.*

object EasingFunctions {

    private val functions = HashMap<EasingTypeCode, EasingFunction>()

    init {
        functions[EasingTypeCode.LINEAR] = LinearEasing()
        functions[EasingTypeCode.CONSTANT] = ConstantEasing()
    }

    operator fun get(easingType: EasingTypeCode): EasingFunction {
        return functions[easingType]!!
    }
}