package io.sparkled.model.animation.effect

enum class EffectTypeCode {

    /**
     * Indicates that no type has been provided.
     */
    NONE,

    /**
     * A single flash from black to full brightness, and back to black.
     */
    FLASH,

    /**
     * Flashing, stationary particles.
     */
    GLITTER,

    /**
     * A line moving along a stage prop.
     */
    LINE,

    /**
     * Two lines moving along a stage prop from a starting point.
     */
    SPLIT_LINE
}