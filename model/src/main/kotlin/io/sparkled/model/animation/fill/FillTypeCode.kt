package io.sparkled.model.animation.fill

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue

enum class FillTypeCode(val displayName: String) {

    /**
     * Indicates that no type has been provided.
     */
    @JsonEnumDefaultValue
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
