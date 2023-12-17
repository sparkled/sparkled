package io.sparkled.renderer.api

import io.sparkled.model.StageModel
import io.sparkled.model.StagePropModel
import io.sparkled.model.animation.ColorList
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.animation.param.HasArguments
import io.sparkled.model.render.RenderedFrame
import io.sparkled.model.render.RenderedStagePropData
import io.sparkled.renderer.SparkledPluginManager
import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.reflect.KClass

data class RenderContext(
    private val pluginManager: SparkledPluginManager,
    val stage: StageModel,
    val framesPerSecond: Int,
    val channel: RenderedStagePropData,
    val frame: RenderedFrame,
    val stageProp: StagePropModel,
    val effect: Effect,
    val progress: Float,
    private val fills: Map<String, SparkledFill>,
    val loadGif: (filename: String) -> List<BufferedImage>,
) {
    val fill = fills[effect.fill.type]
    val pixelCount = stageProp.ledCount

    @Suppress("UNCHECKED_CAST")
    fun <T> getParam(hasArgs: HasArguments, param: Enum<*>, valueType: KClass<T & Any>, default: T & Any): T {
        val values = hasArgs.args[param.name] ?: emptyList()
        return if (values.firstOrNull() == "∴") {
            val scriptType = values.getOrNull(1)
            val script = values.getOrNull(2)

            if (scriptType != "kts") {
                throw RuntimeException("Script type '$scriptType' is not recognised.")
            } else if (script.isNullOrEmpty()) {
                throw RuntimeException("No script was provided for scripted parameter.")
            }

            val result = pluginManager.loadScript(script).execute(this) as? T
            result ?: default
        } else {
            hasArgs.argsCache.getOrPut(param) {
                if (values.isEmpty()) default else when (valueType) {
                    Boolean::class -> (values[0] == "true")
                    Float::class -> values[0].toFloat()
                    Int::class -> values[0].toInt()
                    String::class -> values[0]
                    Color::class -> Color.decode(values[0].lowercase())
                    ColorList::class -> values.mapTo(ColorList(values.size)) { Color.decode(it.lowercase()) }
                    else -> throw RuntimeException("Unsupported parameter type: ${valueType.qualifiedName}.")
                }
            } as T
        }
    }
}

