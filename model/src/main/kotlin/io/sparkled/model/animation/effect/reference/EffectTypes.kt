package io.sparkled.model.animation.effect.reference

import io.sparkled.model.animation.effect.EffectType
import io.sparkled.model.animation.effect.EffectTypeCode
import io.sparkled.model.animation.param.Param
import io.sparkled.model.animation.param.ParamName
import io.sparkled.model.animation.param.ParamType
import io.sparkled.model.util.ParamUtils.param
import java.util.Arrays

object EffectTypes {
    private val TYPES = Arrays.asList(
        effectType(EffectTypeCode.FLASH, "Flash"),
        effectType(
            EffectTypeCode.GLITTER, "Glitter",
            param(ParamName.DENSITY, ParamType.DECIMAL, 50),
            param(ParamName.LIFETIME, ParamType.DECIMAL, 1f),
            param(ParamName.RANDOM_SEED, ParamType.INTEGER, 1)
        ),
        effectType(
            EffectTypeCode.LINE, "Line",
            param(ParamName.LENGTH, ParamType.DECIMAL, 1f)
        ),
        effectType(
            EffectTypeCode.SPLIT_LINE, "Split Line",
            param(ParamName.LENGTH, ParamType.DECIMAL, 1f)
        )
    )

    fun get(): List<EffectType> {
        return TYPES
    }

    private fun effectType(effectType: EffectTypeCode, name: String, vararg params: Param): EffectType {
        return EffectType().setCode(effectType).setName(name).setParams(listOf(*params))
    }
}