package io.sparkled.model.animation.param

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue

enum class ParamCode constructor(val displayName: String) {
    @JsonEnumDefaultValue
    NONE("None"),
    BLEND_HARDNESS("Blend Hardness (%)"),
    BRIGHTNESS("Brightness (%)"),
    COLOR("Color"),
    COLOR_REPETITIONS("Color Repetitions"),
    COLORS("Colors"),
    CYCLE_COUNT("Cycle Count"),
    CYCLES_PER_SECOND("Cycles Per Second"),
    DENSITY("Density (%)"),
    LENGTH("Length"),
    LIFETIME("Lifetime (Seconds)"),
    RANDOM_SEED("Random Seed"),
    SEGMENTS("Number of Segments"),
}
