package io.sparkled.renderer.api

import io.sparkled.model.StageModel
import io.sparkled.model.StagePropModel
import io.sparkled.model.animation.Colors
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
    val fill = fills[effect.fill.type]!!
    val pixelCount = stageProp.ledCount

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getParam(hasArgs: HasArguments, param: String, valueType: KClass<T>): T? {
        val values = hasArgs.args[param] ?: emptyList()
        val firstValue = values.getOrNull(0) ?: ""
        return if (firstValue.startsWith("∴")) {
            val scriptType = firstValue.drop(1)
            val script = values.getOrNull(1)

            if (scriptType != "kts") {
                throw RuntimeException("Script type '$scriptType' is not recognised.")
            } else if (script.isNullOrBlank()) {
                throw RuntimeException("No script was provided for scripted parameter.")
            }

            pluginManager.loadScript(script).execute(this) as? T
        } else {
            hasArgs.argsCache.getOrPut(param) {
                if (values.isEmpty()) null else when (valueType) {
                    Boolean::class -> (values[0] == "true")
                    Float::class -> values[0].toFloat()
                    Int::class -> values[0].toInt()
                    String::class -> values[0]
                    Color::class -> Color.decode(values[0].lowercase())
                    Colors::class -> values.mapTo(Colors(values.size)) { Color.decode(it.lowercase()) }
                    else -> throw RuntimeException("Unsupported parameter type: ${valueType.qualifiedName}.")
                }
            } as? T
        }
    }
}

