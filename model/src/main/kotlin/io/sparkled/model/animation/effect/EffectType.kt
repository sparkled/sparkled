package io.sparkled.model.animation.effect

import io.sparkled.model.animation.param.Param

data class EffectType(
    val code: EffectTypeCode = EffectTypeCode.NONE,
    val name: String = EffectTypeCode.NONE.displayName,
    val params: List<Param> = emptyList()
)
