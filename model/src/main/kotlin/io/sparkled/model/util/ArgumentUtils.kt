package io.sparkled.model.util

import io.sparkled.model.animation.param.Argument
import io.sparkled.model.animation.param.ParamCode

/**
 * Helper functions for arguments.
 */
object ArgumentUtils {

    /**
     * @param code The argument code.
     * @param values The argument values.
     * @return The created argument object.
     */
    fun arg(code: ParamCode, vararg values: Any): Argument {
        return Argument(code, value = values.map { it.toString() })
    }
}
