package io.sparkled.model.animation.fill

enum class FillTypeCode {

    /**
     * Indicates that no type has been provided.
     */
    NONE,

    /**
     * A gradient consisting of one or more colors.
     */
    GRADIENT,

    /**
     * An approximation of a rainbow.
     */
    RAINBOW,

    /**
     * A single color.
     */
    SOLID
}