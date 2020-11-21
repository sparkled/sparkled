package io.sparkled.model.animation.param

/**
 * Denotes an animation class that has arguments.
 */
interface HasArguments {
    val args: Map<String, List<String>>
}
