package io.sparkled.model.animation.easing.reference

import io.sparkled.model.animation.easing.EasingType
import io.sparkled.model.animation.easing.EasingTypeCode
import io.sparkled.model.animation.param.Param
import io.sparkled.model.animation.param.ParamCode
import io.sparkled.model.animation.param.ParamType
import io.sparkled.model.util.ParamUtils.param
import java.util.Arrays

object EasingTypes {
    private val TYPES = Arrays.asList(
        easingType(
            EasingTypeCode.CONSTANT,
            param(ParamCode.PERCENT, ParamType.DECIMAL, 50)
        ),
        easingType(
            EasingTypeCode.LINEAR,
            param(ParamCode.PERCENT_FROM, ParamType.DECIMAL, 0),
            param(ParamCode.PERCENT_TO, ParamType.DECIMAL, 100)
        )
    )

    fun get(): List<EasingType> {
        return TYPES
    }

    private fun easingType(code: EasingTypeCode, vararg params: Param): EasingType {
        return EasingType(code, code.displayName, listOf(*params))
    }
}