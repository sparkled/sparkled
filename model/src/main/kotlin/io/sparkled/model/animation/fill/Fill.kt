package io.sparkled.model.animation.fill

import io.sparkled.model.animation.param.Argument
import io.sparkled.model.animation.param.HasArguments

data class Fill(
    var type: FillTypeCode = FillTypeCode.NONE,
    var args: List<Argument> = emptyList()
) : HasArguments {

    override fun getArguments(): List<Argument> {
        return args
    }
}