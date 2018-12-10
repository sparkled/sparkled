package io.sparkled.model.animation.fill

import io.sparkled.model.animation.param.HasParams
import io.sparkled.model.animation.param.Param

data class Fill(
    var type: FillTypeCode = FillTypeCode.NONE,
    private var params: List<Param> = emptyList()
) : HasParams {

    override fun getParams(): List<Param> {
        return params
    }
}