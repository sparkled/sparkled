package io.sparkled.renderer.util

import io.sparkled.model.animation.param.Argument
import io.sparkled.model.animation.param.HasArguments
import io.sparkled.model.animation.param.ParamCode
import java.awt.Color

object ParamUtils {

    fun getIntegerValue(parent: HasArguments, paramCode: ParamCode, default: Int = 0): Int {
        return getDecimalValue(parent, paramCode, default.toFloat()).toInt()
    }

    fun getDecimalValue(parent: HasArguments, paramCode: ParamCode, default: Float = 0f): Float {
        val argument = getArgument(parent, paramCode)
        val value = argument.value
        return if (value.isEmpty()) default else value[0].toFloat()
    }

    fun getColorValue(parent: HasArguments, paramCode: ParamCode, default: Color = Color.BLACK): Color {
        return getColorsValue(parent, paramCode, default)[0]
    }

    fun getColorsValue(parent: HasArguments, paramCode: ParamCode, default: Color = Color.BLACK): List<Color> {
        val param = getArgument(parent, paramCode)
        return if (param.value.isEmpty()) {
            listOf(default)
        } else {
            param.value.asSequence().map { convertColor(it) }.toList()
        }
    }

    private fun getArgument(parent: HasArguments, paramCode: ParamCode): Argument {
        return parent.getArguments()
            .asSequence()
            .filter { a -> a.code === paramCode }
            .firstOrNull() ?: Argument()
    }

    private fun convertColor(hexColor: String): Color {
        return Color.decode(hexColor.toLowerCase())
    }
}
