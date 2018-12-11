package io.sparkled.model.animation.easing

enum class EasingTypeCode(val displayName: String) {

    /**
     * Indicates that no type has been provided.
     */
    NONE("None"),

    /**
     * A simple, linear easing.
     */
    LINEAR("Linear"),

    /**
     * A linear easing, but frozen at a particular point.
     */
    CONSTANT("Constant")
}