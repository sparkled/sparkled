package io.sparkled.renderer.easing

import io.sparkled.model.animation.easing.EasingTypeCode
import io.sparkled.renderer.easing.function.LinearEasing
import io.sparkled.renderer.easing.function.expo.ExpoOutEasing
import java.util.HashMap

object EasingFunctions {

    private val functions = HashMap<EasingTypeCode, EasingFunction>()

    init {
        functions[EasingTypeCode.EXPO_OUT] = ExpoOutEasing()
        functions[EasingTypeCode.LINEAR] = LinearEasing()
    }

    operator fun get(easingType: EasingTypeCode): EasingFunction {
        return functions[easingType]!!
    }
}