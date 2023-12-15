package io.sparkled.renderer.util

import io.sparkled.model.animation.param.HasArguments
import java.awt.Color
import java.util.Locale

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

    fun getString(parent: HasArguments, paramCode: String, default: String = ""): String {
        val values = getArgumentValues(parent, paramCode)
        return if (values.isEmpty()) default else values[0]
    }

    private fun getArgumentValues(parent: HasArguments, paramCode: String): List<String> {
        return parent.args[paramCode] ?: emptyList()
    }
}
