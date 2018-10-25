package io.sparkled.model.animation.effect.reference

import io.sparkled.model.animation.effect.EffectType
import io.sparkled.model.animation.effect.EffectTypeCode
import io.sparkled.model.animation.param.Param
import io.sparkled.model.animation.param.ParamName
import io.sparkled.model.animation.param.ParamType

import java.util.Arrays

object EffectTypes {
    private val TYPES = Arrays.asList(
            effectType(EffectTypeCode.FLASH, "Flash"),
            effectType(EffectTypeCode.LINE, "Line",
                    param(ParamName.LENGTH, ParamType.INTEGER).setValue(1)
            ),
            effectType(EffectTypeCode.SPLIT_LINE, "Split Line",
                    param(ParamName.LENGTH, ParamType.INTEGER).setValue(1)
            )
    )

    fun get(): List<EffectType> {
        return TYPES
    }

    private fun effectType(effectType: EffectTypeCode, name: String, vararg params: Param): EffectType {
        return EffectType().setCode(effectType).setName(name).setParams(Arrays.asList(params))
    }

    private fun param(paramName: ParamName, type: ParamType): Param {
        return Param().setName(paramName).setType(type)
    }
}