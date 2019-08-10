package io.sparkled.model.animation.fill

import io.sparkled.model.animation.param.Param

data class FillType(
    val code: FillTypeCode = FillTypeCode.NONE,
    val name: String = FillTypeCode.NONE.displayName,
    val params: List<Param> = emptyList()
)
