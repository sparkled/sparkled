package io.sparkled.model.animation.easing

import io.sparkled.model.animation.param.HasParams
import io.sparkled.model.animation.param.Param

data class Easing(
    var type: EasingTypeCode = EasingTypeCode.LINEAR,
    private var params: List<Param> = emptyList()
) : HasParams {

    override fun getParams(): List<Param> {
        return this.params
    }
}