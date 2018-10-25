package io.sparkled.model.animation.param

enum class ParamName private constructor(val name: String) {
    COLOR("Color"),
    CYCLE_COUNT("Cycle Count"),
    CYCLES_PER_SECOND("Cycles Per Second"),
    LENGTH("Length"),
    PERCENT("Percent")
}
