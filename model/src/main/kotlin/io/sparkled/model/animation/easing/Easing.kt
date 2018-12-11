package io.sparkled.model.animation.easing

import io.sparkled.model.animation.param.Argument
import io.sparkled.model.animation.param.HasArguments

data class Easing(
    var type: EasingTypeCode = EasingTypeCode.LINEAR,
    var args: List<Argument> = emptyList()
) : HasArguments {

    override fun getArguments(): List<Argument> {
        return this.args
    }
}