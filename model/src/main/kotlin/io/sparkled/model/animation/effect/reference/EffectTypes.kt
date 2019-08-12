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
            EffectTypeCode.BUILD_LINE,
            param(ParamCode.SEGMENTS, ParamType.INTEGER, 4)
        ),
        effectType(
            EffectTypeCode.GLITTER,
            param(ParamCode.DENSITY, ParamType.INTEGER, 50),
            param(ParamCode.LIFETIME, ParamType.DECIMAL, 1),
            param(ParamCode.RANDOM_SEED, ParamType.INTEGER, 1)
        ),
        effectType(
            EffectTypeCode.LINE,
            param(ParamCode.LENGTH, ParamType.DECIMAL, 1)
        ),
        effectType(
            EffectTypeCode.SPLIT_LINE,
            param(ParamCode.LENGTH, ParamType.DECIMAL, 1)
        )
    )

    fun get(): List<EffectType> {
        return TYPES
    }

    private fun effectType(code: EffectTypeCode, vararg params: Param): EffectType {
        return EffectType(code, code.displayName, listOf(*params))
    }
}
