package io.sparkled.renderer

import io.sparkled.model.config.SparkledConfig
import io.sparkled.renderer.api.SparkledEasing
import io.sparkled.renderer.api.SparkledEffect
import io.sparkled.renderer.api.SparkledFill
import io.sparkled.renderer.api.SparkledPlugin
import io.sparkled.renderer.easing.function.ExpoOutEasing
import io.sparkled.renderer.easing.function.LinearEasing
import io.sparkled.renderer.effect.FlashEffect
import io.sparkled.renderer.effect.GifEffect
import io.sparkled.renderer.effect.GlitterEffect
import io.sparkled.renderer.effect.SolidEffect
import io.sparkled.renderer.effect.line.BuildLineEffect
import io.sparkled.renderer.effect.line.FireEffect
import io.sparkled.renderer.effect.line.LineEffect
import io.sparkled.renderer.effect.line.SplitLineEffect
import io.sparkled.renderer.fill.GradientFill
import io.sparkled.renderer.fill.RainbowFill
import io.sparkled.renderer.fill.SingleColorFill
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import javax.script.ScriptEngineManager

@Singleton
class SparkledPluginManager(
    private val sparkledConfig: SparkledConfig
) {
    private val scriptEngine = ScriptEngineManager().getEngineByExtension("kts")
    private val defaultEasings = listOf(LinearEasing, ExpoOutEasing)
    private val defaultEffects: List<SparkledEffect<*>> = listOf(BuildLineEffect, FireEffect, FlashEffect, GifEffect, GlitterEffect, LineEffect, SplitLineEffect, SolidEffect)
    private val defaultFills = listOf(GradientFill, RainbowFill, SingleColorFill)

    val easings = AtomicReference<SortedMap<String, SparkledEasing>>(sortedMapOf())
    val effects = AtomicReference<SortedMap<String, SparkledEffect<*>>>(sortedMapOf())
    val fills = AtomicReference<SortedMap<String, SparkledFill>>(sortedMapOf())

    fun reloadPlugins() {
        logger.info("Reloading plugins.")
        val easingPlugins = reloadTypedPlugins<SparkledEasing>("easings")
        val effectPlugins = reloadTypedPlugins<SparkledEffect<*>>("effects")
        val fillPlugins = reloadTypedPlugins<SparkledFill>("fills")

        easings.set((defaultEasings + easingPlugins).associateBy { it.id }.toSortedMap())
        effects.set((defaultEffects + effectPlugins).associateBy { it.id }.toSortedMap())
        fills.set((defaultFills + fillPlugins).associateBy { it.id }.toSortedMap())

        logger.info("Finished reloading plugins.")
    }

    private inline fun <reified T : SparkledPlugin> reloadTypedPlugins(subdirectory: String): List<T> {
        val pluginType = T::class.simpleName
        logger.info("Loading $pluginType plugins.")
        return with(scriptEngine) {
            val pluginScripts = File("${sparkledConfig.dataFolderPath}/${sparkledConfig.pluginFolderName}/$subdirectory")
                .listFiles { _, name -> name.endsWith(".kts") } ?: emptyArray()

            // TODO add unit tests for custom effects
            // TODO security management, prevent malicious code?
            pluginScripts.mapNotNull {
                try {
                    val plugin = eval(it.readText()) as T
                    logger.info("Loaded $pluginType ${plugin.id} (${plugin.name})")
                    plugin
                } catch (e: Exception) {
                    logger.error("Failed to load $pluginType from ${it.name}, skipping.", e)
                    null
                }
            }
        }.apply {
            logger.info("Loading $size $pluginType plugin(s).")
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SparkledPluginManager::class.java)
    }
}
