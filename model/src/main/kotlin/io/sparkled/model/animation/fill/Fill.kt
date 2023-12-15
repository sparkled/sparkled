package io.sparkled.model.animation.fill

import io.sparkled.model.animation.ColorList
import io.sparkled.model.animation.param.HasArguments
import io.sparkled.model.annotation.GenerateClientType
import java.awt.Color
import kotlin.reflect.KClass

@GenerateClientType
data class Fill(
    val type: String = "NONE",
    val blendMode: BlendMode = BlendMode.NORMAL,
    override val args: Map<String, List<String>> = emptyMap(),
) : HasArguments {
    fun <T> getParam(param: Enum<*>, valueType: KClass<T & Any>, default: T & Any): T {
        @Suppress("UNCHECKED_CAST")
        return cache.getOrPut(param) {
            val values = args[param.name]

            if (values.isNullOrEmpty()) default else when {
                valueType == Boolean::class -> (values[0] == "true")
                valueType == Float::class -> values[0].toFloat()
                valueType == Int::class -> values[0].toInt()
                valueType == String::class -> values[0]
                valueType == Color::class -> Color.decode(values[0].lowercase())
                valueType == ColorList::class -> values.mapTo(ColorList(values.size)) { Color.decode(it.lowercase()) }
                else -> throw RuntimeException("Unsupported parameter type: ${valueType.qualifiedName}.")
            }
        } as T
    }

    private val cache = mutableMapOf<Enum<*>, Any>()
}
