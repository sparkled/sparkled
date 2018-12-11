package io.sparkled.model.animation.param

data class Argument(
    var code: ParamCode = ParamCode.NONE,
    var value: List<String> = emptyList()
)
