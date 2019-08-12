package io.sparkled.model.animation.fill

enum class FillTypeCode(val displayName: String) {

    /**
     * Indicates that no type has been provided.
     */
    NONE("None"),

    /**
     * A gradient consisting of one or more colors.
     */
    GRADIENT("Gradient"),

    /**
     * An approximation of a rainbow.
     */
    RAINBOW("Rainbow"),

    /**
     * A single color.
     */
    SOLID("Solid")
}
