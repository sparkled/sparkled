package io.sparkled.model.animation.easing.reference

import io.sparkled.model.animation.easing.EasingType
import io.sparkled.model.animation.easing.EasingTypeCode
import io.sparkled.model.animation.param.Param
import io.sparkled.model.animation.param.ParamName
import io.sparkled.model.animation.param.ParamType
import java.util.Arrays

object EasingTypes {
    private val TYPES = Arrays.asList(
        easingType(
            EasingTypeCode.CONSTANT, "Constant",
            param(ParamName.PERCENT, ParamType.DECIMAL).setValue(50f)
        ),
        easingType(EasingTypeCode.LINEAR, "Linear")
    )

    fun get(): List<EasingType> {
        return TYPES
    }

    private fun easingType(easingType: EasingTypeCode, name: String, vararg params: Param): EasingType {
        return EasingType().setCode(easingType).setName(name).setParams(arrayListOf(*params))
    }

    private fun param(paramName: ParamName, type: ParamType): Param {
        return Param().setName(paramName).setType(type)
    }
}