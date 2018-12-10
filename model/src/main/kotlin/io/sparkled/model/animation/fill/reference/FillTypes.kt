package io.sparkled.model.animation.fill.reference

import io.sparkled.model.animation.fill.FillType
import io.sparkled.model.animation.fill.FillTypeCode
import io.sparkled.model.animation.param.Param
import io.sparkled.model.animation.param.ParamName
import io.sparkled.model.animation.param.ParamType
import io.sparkled.model.util.ParamUtils.param
import java.util.Arrays

object FillTypes {
    private val TYPES = Arrays.asList(
        fill(
            FillTypeCode.GRADIENT, "Gradient",
            param(ParamName.COLORS, ParamType.COLORS, "#ff0000", "#0000ff"),
            param(ParamName.BLEND_HARDNESS, ParamType.DECIMAL, 0f),
            param(ParamName.CYCLES_PER_SECOND, ParamType.DECIMAL, 0f)
        ),
        fill(
            FillTypeCode.RAINBOW, "Rainbow",
            param(ParamName.BRIGHTNESS, ParamType.DECIMAL, 100f),
            param(ParamName.CYCLE_COUNT, ParamType.DECIMAL, 1f),
            param(ParamName.CYCLES_PER_SECOND, ParamType.DECIMAL, .5f)
        ),
        fill(
            FillTypeCode.SOLID, "Solid",
            param(ParamName.COLOR, ParamType.COLOR, "#ff0000")
        )
    )

    fun get(): List<FillType> {
        return TYPES
    }

    private fun fill(fillType: FillTypeCode, name: String, vararg params: Param): FillType {
        return FillType().setCode(fillType).setName(name).setParams(listOf(*params))
    }
}