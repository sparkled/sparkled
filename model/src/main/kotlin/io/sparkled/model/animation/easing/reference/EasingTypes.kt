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
            param(ParamCode.PERCENT, ParamType.DECIMAL, 50f)
        ),
        easingType(EasingTypeCode.LINEAR)
    )

    fun get(): List<EasingType> {
        return TYPES
    }

    private fun easingType(type: EasingTypeCode, vararg params: Param): EasingType {
        return EasingType().setCode(type).setName(type.displayName).setParams(listOf(*params))
    }
}