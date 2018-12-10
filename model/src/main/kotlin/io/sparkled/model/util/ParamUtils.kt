package io.sparkled.model.util

import io.sparkled.model.animation.param.Param
import io.sparkled.model.animation.param.ParamName
import io.sparkled.model.animation.param.ParamType

/**
 * Helper functions for params.
 */
object ParamUtils {

    /**
     * @param name The parameter name.
     * @param type The parameter type.
     * @param value The parameter value.
     * @return The created parameter object.
     */
    fun param(name: ParamName, type: ParamType = ParamType.NONE, value: Any): Param {
        return Param(name, type, listOf(value.toString()))
    }

    /**
     * @param name The parameter name.
     * @param type The parameter type.
     * @param value The parameter value.
     * @return The created parameter object.
     */
    fun param(name: ParamName, type: ParamType = ParamType.NONE, vararg value: Any): Param {
        return Param(name, type, value = value.map { it.toString() })
    }
}
