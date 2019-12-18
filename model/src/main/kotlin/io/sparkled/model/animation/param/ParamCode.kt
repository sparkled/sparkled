package io.sparkled.model.animation.param

enum class ParamCode constructor(val displayName: String) {
    NONE("None"),
    BLEND_HARDNESS("Blend Hardness (%)"),
    BRIGHTNESS("Brightness (%)"),
    COLOR("Color"),
    COLORS("Colors"),
    CYCLE_COUNT("Cycle Count"),
    CYCLES_PER_SECOND("Cycles Per Second"),
    DENSITY("Density (%)"),
    LENGTH("Length"),
    LIFETIME("Lifetime (Seconds)"),
    RANDOM_SEED("Random Seed"),
    SEGMENTS("Number of Segments"),
}
