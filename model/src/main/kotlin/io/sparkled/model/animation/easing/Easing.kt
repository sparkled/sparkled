package io.sparkled.model.animation.easing

import io.sparkled.model.animation.param.Argument
import io.sparkled.model.animation.param.HasArguments

data class Easing(
    val type: EasingTypeCode = EasingTypeCode.LINEAR,
    val start: Float = 0f,
    val end: Float = 100f,
    val args: List<Argument> = emptyList()
) : HasArguments {

    override fun getArguments(): List<Argument> {
        return this.args
    }
}