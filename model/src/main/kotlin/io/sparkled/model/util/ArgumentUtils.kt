package io.sparkled.model.util

/**
 * Helper functions for arguments.
 */
object ArgumentUtils {

    /**
     * @param code The argument code.
     * @param values The argument values.
     * @return The created argument object.
     */
    fun arg(code: String, vararg values: Any): Pair<String, List<String>> {
        return Pair(code, values.map { it.toString() })
    }
}
