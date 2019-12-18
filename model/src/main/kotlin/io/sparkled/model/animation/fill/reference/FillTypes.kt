package io.sparkled.model.animation.fill.reference

import io.sparkled.model.animation.fill.FillType
import io.sparkled.model.animation.fill.FillTypeCode
import io.sparkled.model.animation.param.Param
import io.sparkled.model.animation.param.ParamCode
import io.sparkled.model.animation.param.ParamType
import io.sparkled.model.util.ParamUtils.param
import java.util.Arrays

object FillTypes {
    private val TYPES = Arrays.asList(
        fill(
            FillTypeCode.GRADIENT,
            param(ParamCode.COLORS, ParamType.COLORS, "#ff0000", "#0000ff"),
            param(ParamCode.COLOR_REPETITIONS, ParamType.INTEGER, 1),
            param(ParamCode.BLEND_HARDNESS, ParamType.DECIMAL, 0),
            param(ParamCode.CYCLES_PER_SECOND, ParamType.DECIMAL, 0)
        ),
        fill(
            FillTypeCode.RAINBOW,
            param(ParamCode.BRIGHTNESS, ParamType.DECIMAL, 100),
            param(ParamCode.CYCLE_COUNT, ParamType.DECIMAL, 1),
            param(ParamCode.CYCLES_PER_SECOND, ParamType.DECIMAL, .5f)
        ),
        fill(
            FillTypeCode.SOLID,
            param(ParamCode.COLOR, ParamType.COLOR, "#ff0000")
        )
    )

    fun get(): List<FillType> {
        return TYPES
    }

    private fun fill(code: FillTypeCode, vararg params: Param): FillType {
        return FillType(code, code.displayName, listOf(*params))
    }
}
