package io.sparkled.model.util

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
    fun arg(code: ParamCode, vararg values: Any): Pair<ParamCode, List<String>> {
        return Pair(code, values.map { it.toString() })
    }
}
