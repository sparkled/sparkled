package io.sparkled.renderer.easing

import io.sparkled.model.animation.easing.EasingTypeCode
import io.sparkled.renderer.easing.function.ConstantEasing
import io.sparkled.renderer.easing.function.LinearEasing

import java.util.HashMap

object EasingFunctions {

    private val functions = HashMap()

    init {
        functions.put(EasingTypeCode.LINEAR, LinearEasing())
        functions.put(EasingTypeCode.CONSTANT, ConstantEasing())
    }

    operator fun get(easingType: EasingTypeCode): EasingFunction {
        return functions.get(easingType)
    }
}