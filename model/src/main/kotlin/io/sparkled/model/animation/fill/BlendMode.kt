package io.sparkled.model.animation.fill

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue

enum class BlendMode(val displayName: String) {

    /**
     * Top layer pixels will replace bottom layer pixels.
     */
    @JsonEnumDefaultValue
    NORMAL("Normal"),

    /**
     * Top layer pixel RGB values are added to bottom layer RGB values.
     * Values larger than 255 are clamped to 255.
     */
    ADD("Add"),

    /**
     * Top layer pixel RGB values are subtracted from bottom layer RGB values.
     * Values less than 0 are clamped to 0.
     */
    SUBTRACT("Subtract"),

    /**
     * The inverse of [SUBTRACT], i.e. bottom layer RGB values are subtracted from top layer RGB values.
     * Values less than 0 are clamped to 0.
     */
    ALPHA_MASK("Alpha Mask")
}
