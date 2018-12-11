package io.sparkled.model.animation.easing

import io.sparkled.model.animation.param.Param

data class EasingType(
    val code: EasingTypeCode = EasingTypeCode.NONE,
    val name: String = EasingTypeCode.NONE.displayName,
    val params: List<Param> = emptyList()
)
