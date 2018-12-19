package io.sparkled.renderer.util

import io.sparkled.model.animation.param.HasArguments
import io.sparkled.model.animation.param.ParamCode
import java.awt.Color

object ParamUtils {

    fun getIntegerValue(parent: HasArguments, paramCode: ParamCode, default: Int = 0): Int {
        return getDecimalValue(parent, paramCode, default.toFloat()).toInt()
    }

    fun getDecimalValue(parent: HasArguments, paramCode: ParamCode, default: Float = 0f): Float {
        val values = getArgumentValues(parent, paramCode)
        return if (values.isEmpty()) default else values[0].toFloat()
    }

    fun getColorValue(parent: HasArguments, paramCode: ParamCode, default: Color = Color.BLACK): Color {
        return getColorsValue(parent, paramCode, default)[0]
    }

    fun getColorsValue(parent: HasArguments, paramCode: ParamCode, default: Color = Color.BLACK): List<Color> {
        val values = getArgumentValues(parent, paramCode)
        return if (values.isEmpty()) {
            listOf(default)
        } else {
            values.map { convertColor(it) }.toList()
        }
    }

    private fun getArgumentValues(parent: HasArguments, paramCode: ParamCode): List<String> {
        return parent.getArguments()[paramCode] ?: emptyList()
    }

    private fun convertColor(hexColor: String): Color {
        return Color.decode(hexColor.toLowerCase())
    }
}
