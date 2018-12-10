package io.sparkled.model.animation.param

data class Param(
    var name: ParamName = ParamName.NONE,
    var type: ParamType = ParamType.NONE,
    var value: List<String> = emptyList()
)
