package io.sparkled.model.animation.param

enum class ParamName constructor(val displayName: String) {
    NONE("None"),
    BLEND_HARDNESS("Blend Hardness (%)"),
    BRIGHTNESS("Brightness (%)"),
    DENSITY("Density (%)"),
    COLOR("Color"),
    COLORS("Colors"),
    CYCLE_COUNT("Cycle Count"),
    CYCLES_PER_SECOND("Cycles Per Second"),
    LENGTH("Length"),
    LIFETIME("Lifetime (Seconds)"),
    PERCENT("Percent"),
    RANDOM_SEED("Random Seed"),
}
