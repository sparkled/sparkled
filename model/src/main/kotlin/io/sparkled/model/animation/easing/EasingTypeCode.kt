package io.sparkled.model.animation.easing

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue

enum class EasingTypeCode(val displayName: String) {

    /**
     * Indicates that no type has been provided.
     */
    @JsonEnumDefaultValue
    NONE("None"),

    /**
     * An easing with very fast acceleration.
     */
    EXPO_OUT("Exponential (Out)"),

    /**
     * A simple, linear easing.
     */
    LINEAR("Linear")
}
