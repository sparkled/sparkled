package io.sparkled.model.animation.param

data class Param(
    val code: String,
    val displayName: String,
    val type: ParamType = ParamType.NONE,
    val defaultValue: List<String> = emptyList(),
) {
    companion object {
        fun boolean(code: String, displayName: String, defaultValue: Boolean): Param {
            return Param(code, displayName, ParamType.BOOLEAN, listOf(defaultValue.toString()))
        }

        fun color(code: String, displayName: String, defaultValue: String): Param {
            return Param(code, displayName, ParamType.COLOR, listOf(defaultValue))
        }

        fun colors(code: String, displayName: String, defaultValue: List<String>): Param {
            return Param(code, displayName, ParamType.COLORS, defaultValue)
        }

        fun decimal(code: String, displayName: String, defaultValue: Double): Param {
            return Param(code, displayName, ParamType.DECIMAL, listOf(defaultValue.toString()))
        }

        fun int(code: String, displayName: String, defaultValue: Int): Param {
            return Param(code, displayName, ParamType.INTEGER, listOf(defaultValue.toString()))
        }

        fun string(code: String, displayName: String, defaultValue: String): Param {
            return Param(code, displayName, ParamType.STRING, listOf(defaultValue))
        }
    }
}
