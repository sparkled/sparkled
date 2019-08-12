package io.sparkled.model.animation.easing

import com.fasterxml.jackson.annotation.JsonIgnore
import io.sparkled.model.animation.param.HasArguments
import io.sparkled.model.animation.param.ParamCode

data class Easing(
    val type: EasingTypeCode = EasingTypeCode.LINEAR,
    val start: Float = 0f,
    val end: Float = 100f,
    val args: Map<ParamCode, List<String>> = emptyMap()
) : HasArguments {

    @JsonIgnore
    override fun getArguments(): Map<ParamCode, List<String>> {
        return this.args
    }
}
