package io.sparkled.model.util

import io.sparkled.model.animation.param.Param
import io.sparkled.model.animation.param.ParamCode
import io.sparkled.model.animation.param.ParamType

/**
 * Helper functions for params.
 */
object ParamUtils {

    /**
     * @param code The parameter code.
     * @param type The parameter type.
     * @param defaultValue The default parameter value.
     * @return The created parameter object.
     */
    fun param(code: ParamCode, type: ParamType = ParamType.NONE, defaultValue: Any): Param {
        return Param(code, code.displayName, type, listOf(defaultValue.toString()))
    }

    /**
     * @param code The parameter code.
     * @param type The parameter type.
     * @param defaultValues The default parameter values.
     * @return The created parameter object.
     */
    fun param(code: ParamCode, type: ParamType = ParamType.NONE, vararg defaultValues: Any): Param {
        return Param(code, code.displayName, type, defaultValue = defaultValues.map { it.toString() })
    }
}
