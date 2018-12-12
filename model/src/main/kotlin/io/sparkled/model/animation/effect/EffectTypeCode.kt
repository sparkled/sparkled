package io.sparkled.model.animation.effect

enum class EffectTypeCode(val displayName: String) {

    /**
     * Indicates that no type has been provided.
     */
    NONE("None"),

    /**
     * A single flash from black to full brightness, and back to black.
     */
    FLASH("Flash"),

    /**
     * Flashing, stationary particles.
     */
    GLITTER("Glitter"),

    /**
     * A line built up from falling segments.
     */
    BUILD_LINE("Build Line"),

    /**
     * A line moving along a stage prop.
     */
    LINE("Line"),

    /**
     * Two lines moving along a stage prop from a starting point.
     */
    SPLIT_LINE("Split Line")
}