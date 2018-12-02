package io.sparkled.model.animation.fill.reference

import io.sparkled.model.animation.fill.FillType
import io.sparkled.model.animation.fill.FillTypeCode
import io.sparkled.model.animation.param.Param
import io.sparkled.model.animation.param.ParamName
import io.sparkled.model.animation.param.ParamType
import java.util.Arrays

object FillTypes {
    private val TYPES = Arrays.asList(
        fill(
            FillTypeCode.GRADIENT, "Gradient",
            param(ParamName.COLORS, ParamType.COLORS).setValue(listOf("#ff0000", "#0000ff")),
            param(ParamName.BLEND_HARDNESS, ParamType.DECIMAL).setValue(0)
        ),
        fill(
            FillTypeCode.RAINBOW, "Rainbow",
            param(ParamName.BRIGHTNESS, ParamType.DECIMAL).setValue(100),
            param(ParamName.CYCLE_COUNT, ParamType.DECIMAL).setValue(1),
            param(ParamName.CYCLES_PER_SECOND, ParamType.DECIMAL).setValue(.5f)
        ),
        fill(
            FillTypeCode.SOLID, "Solid",
            param(ParamName.COLOR, ParamType.COLOR).setValue("#ff0000")
        )
    )

    fun get(): List<FillType> {
        return TYPES
    }

    private fun fill(fillType: FillTypeCode, name: String, vararg params: Param): FillType {
        return FillType().setCode(fillType).setName(name).setParams(arrayListOf(*params))
    }

    private fun param(paramName: ParamName, type: ParamType): Param {
        return Param().setName(paramName).setType(type)
    }
}