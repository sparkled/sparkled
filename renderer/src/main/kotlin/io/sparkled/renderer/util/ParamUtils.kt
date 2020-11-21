package io.sparkled.renderer.util

import io.sparkled.model.animation.param.HasArguments
import java.awt.Color

object ParamUtils {

    fun getBoolean(parent: HasArguments, paramCode: String, default: Boolean = false): Boolean {
        val values = getArgumentValues(parent, paramCode)
        return if (values.isEmpty()) default else values[0] == "true"
    }

    fun getInt(parent: HasArguments, paramCode: String, default: Int = 0): Int {
        return getFloat(parent, paramCode, default.toFloat()).toInt()
    }

    fun getFloat(parent: HasArguments, paramCode: String, default: Float = 0f): Float {
        val values = getArgumentValues(parent, paramCode)
        return if (values.isEmpty()) default else values[0].toFloat()
    }

    fun getColorValue(parent: HasArguments, paramCode: String, default: Color = Color.BLACK): Color {
        return getColors(parent, paramCode, default)[0]
    }

    fun getColors(parent: HasArguments, paramCode: String, default: Color = Color.BLACK): List<Color> {
        val values = getArgumentValues(parent, paramCode)
        return if (values.isEmpty()) {
            listOf(default)
        } else {
            values.map { convertColor(it) }.toList()
        }
    }

    private fun getArgumentValues(parent: HasArguments, paramCode: String): List<String> {
        return parent.args[paramCode] ?: emptyList()
    }

    private fun convertColor(hexColor: String): Color {
        return Color.decode(hexColor.toLowerCase())
    }
}
