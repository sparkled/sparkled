package io.sparkled.model.animation.easing

enum class EasingTypeCode {

    /**
     * Indicates that no type has been provided.
     */
    NONE,

    /**
     * A simple, linear easing.
     */
    LINEAR,

    /**
     * A linear easing, but frozen at a particular point.
     */
    CONSTANT
}