package io.sparkled.model.animation.param

/**
 * Denotes an animation class that has arguments.
 */
interface HasArguments {
    fun getArguments(): Map<ParamCode, List<String>>
}
