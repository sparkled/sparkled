package io.sparkled.model.animation.fill

import io.sparkled.model.animation.param.HasArguments
import io.sparkled.model.animation.param.ParamCode

data class Fill(
    var type: FillTypeCode = FillTypeCode.NONE,
    var args: Map<ParamCode, List<String>> = emptyMap()
) : HasArguments {

    override fun getArguments(): Map<ParamCode, List<String>> {
        return args
    }
}
