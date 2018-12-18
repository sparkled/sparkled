package io.sparkled.model.animation.easing.reference

import io.sparkled.model.animation.easing.EasingType
import io.sparkled.model.animation.easing.EasingTypeCode
import io.sparkled.model.animation.param.Param
import java.util.Arrays

object EasingTypes {
    private val TYPES = Arrays.asList(
        easingType(EasingTypeCode.EXPO_OUT),
        easingType(EasingTypeCode.LINEAR)
    )

    fun get(): List<EasingType> {
        return TYPES
    }

    private fun easingType(code: EasingTypeCode, vararg params: Param): EasingType {
        return EasingType(code, code.displayName, listOf(*params))
    }
}