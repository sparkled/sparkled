package io.sparkled.renderer.util

import io.sparkled.model.animation.param.HasParams
import io.sparkled.model.animation.param.Param
import io.sparkled.model.animation.param.ParamName
import java.awt.Color

object ParamUtils {

    fun getIntegerValue(parent: HasParams, paramName: ParamName, default: Int = 0): Int {
        return getDecimalValue(parent, paramName, default.toFloat()).toInt()
    }

    fun getDecimalValue(parent: HasParams, paramName: ParamName, default: Float = 0f): Float {
        val param = getParam(parent, paramName)
        val value = param.getValue()
        return if (value.isEmpty()) default else value[0]!!.toFloat()
    }

    fun getColorValue(parent: HasParams, paramName: ParamName, default: Color = Color.BLACK): Color {
        return getColorsValue(parent, paramName, default)[0]
    }

    fun getColorsValue(parent: HasParams, paramName: ParamName, default: Color = Color.BLACK): List<Color> {
        val param = getParam(parent, paramName)
        return if (param.getValue().isEmpty()) {
            listOf(default)
        } else {
            param.getValue().asSequence().map { convertColor(it!!) }.toList()
        }
    }

    private fun getParam(parent: HasParams, paramName: ParamName): Param {
        return parent.getParams()
            .asSequence()
            .filter { p -> paramName === p.getName() }
            .firstOrNull() ?: Param()
    }

    private fun convertColor(hexColor: String): Color {
        return Color.decode(hexColor.toLowerCase())
    }
}
