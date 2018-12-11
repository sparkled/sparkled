package io.sparkled.model.animation.effect.reference

import io.sparkled.model.animation.effect.EffectType
import io.sparkled.model.animation.effect.EffectTypeCode
import io.sparkled.model.animation.param.Param
import io.sparkled.model.animation.param.ParamCode
import io.sparkled.model.animation.param.ParamType
import io.sparkled.model.util.ParamUtils.param
import java.util.Arrays

object EffectTypes {
    private val TYPES = Arrays.asList(
        effectType(EffectTypeCode.FLASH),
        effectType(
            EffectTypeCode.GLITTER,
            param(ParamCode.DENSITY, ParamType.INTEGER, 50),
            param(ParamCode.LIFETIME, ParamType.DECIMAL, 1f),
            param(ParamCode.RANDOM_SEED, ParamType.INTEGER, 1)
        ),
        effectType(
            EffectTypeCode.LINE,
            param(ParamCode.LENGTH, ParamType.DECIMAL, 1f)
        ),
        effectType(
            EffectTypeCode.SPLIT_LINE,
            param(ParamCode.LENGTH, ParamType.DECIMAL, 1f)
        )
    )

    fun get(): List<EffectType> {
        return TYPES
    }

    private fun effectType(type: EffectTypeCode, vararg params: Param): EffectType {
        return EffectType().setCode(type).setName(type.displayName).setParams(listOf(*params))
    }
}