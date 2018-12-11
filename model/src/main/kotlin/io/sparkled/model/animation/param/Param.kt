package io.sparkled.model.animation.param

data class Param(
    val code: ParamCode = ParamCode.NONE,
    val displayName: String = ParamCode.NONE.displayName,
    val type: ParamType = ParamType.NONE,
    val defaultValue: List<String> = emptyList()
)
