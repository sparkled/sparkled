package io.sparkled.renderer.util

import io.sparkled.model.animation.param.HasParams
import io.sparkled.model.animation.param.Param
import io.sparkled.model.animation.param.ParamName

import java.awt.*

import java.util.stream.Collectors.toList

object ParamUtils {

    fun getIntegerValue(parent: HasParams, paramName: ParamName): Int {
        val param = getParam(parent, paramName)
        val value = param.getValue()
        return Integer.parseInt(value.get(0))
    }

    fun getDecimalValue(parent: HasParams, paramName: ParamName): Float {
        val param = getParam(parent, paramName)
        val value = param.getValue()
        return Float.parseFloat(value.get(0))
    }

    fun getColorValue(parent: HasParams, paramName: ParamName): Color {
        return getColorsValue(parent, paramName)[0]
    }

    private fun getColorsValue(parent: HasParams, paramName: ParamName): List<Color> {
        val param = getParam(parent, paramName)
        val values = param.getValue()

        return values.stream()
                .map(???({ convertColor(it) }))
        .collect(toList())
    }

    private fun getParam(parent: HasParams, paramName: ParamName): Param {
        return parent.getParams()
                .stream()
                .filter({ p -> paramName === p.getName() })
                .findFirst()
                .orElse(Param())
    }

    private fun convertColor(hexColor: String): Color {
        return Color.decode(hexColor.toLowerCase())
    }
}
