package io.sparkled.model.animation.fill

import com.fasterxml.jackson.annotation.JsonIgnore
import io.sparkled.model.animation.param.HasArguments
import io.sparkled.model.animation.param.ParamCode

data class Fill(
    val type: FillTypeCode = FillTypeCode.NONE,
    val blendMode: BlendMode = BlendMode.NORMAL,
    val args: Map<ParamCode, List<String>> = emptyMap()
) : HasArguments {

    @JsonIgnore
    override fun getArguments(): Map<ParamCode, List<String>> {
        return args
    }
}
