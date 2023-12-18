package io.sparkled.renderer

import io.sparkled.common.logging.getLogger
import io.sparkled.model.config.SparkledConfig
import io.sparkled.renderer.api.SparkledEasing
import io.sparkled.renderer.api.SparkledEffect
import io.sparkled.renderer.api.SparkledFill
import io.sparkled.renderer.api.SparkledPlugin
import io.sparkled.renderer.api.scripting.SparkledScript
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
import java.io.File
import java.util.SortedMap
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference
import javax.script.ScriptEngineManager
import kotlin.time.measureTimedValue

@Singleton
class SparkledPluginManager(
    private val sparkledConfig: SparkledConfig,
) {
    private val defaultEasings = listOf(LinearEasing, ExpoOutEasing)
    private val defaultEffects: List<SparkledEffect<*>> = listOf(
        BuildLineEffect,
        FireEffect,
        FlashEffect,
        GifEffect,
        GlitterEffect,
        LineEffect,
        SplitLineEffect,
        SolidEffect,
    )
    private val defaultFills = listOf(GradientFill, RainbowFill, SingleColorFill)

    val easings = AtomicReference<SortedMap<String, SparkledEasing>>(sortedMapOf())
    val effects = AtomicReference<SortedMap<String, SparkledEffect<*>>>(sortedMapOf())
    val fills = AtomicReference<SortedMap<String, SparkledFill>>(sortedMapOf())
    private val scripts = ConcurrentHashMap<String, SparkledScript<*>>()

    fun reloadPlugins() {
        // Warm up the script engine as there may not be any installed plugins, and the script engine can be used as
        // part of live paint etc., which would cause a delay of several seconds upon first usage.
        scriptEngine.eval("1 + 2")
        logger.info("Pre-warmed script engine.")

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

        val pluginScripts =
            File("${sparkledConfig.dataFolderPath}/${sparkledConfig.pluginFolderName}/$subdirectory")
                .listFiles { _, name -> name.endsWith(".kts") } ?: emptyArray()
        logger.info("Loading ${pluginScripts.size} $pluginType plugin(s).")

        // TODO add unit tests for custom effects
        // TODO security management, prevent malicious code?
        return pluginScripts.mapNotNull {
            try {
                val plugin = scriptEngine.eval(it.readText()) as T
                logger.info("Loaded $pluginType ${plugin.id} (${plugin.name})")
                plugin
            } catch (e: Exception) {
                logger.error("Failed to load $pluginType from ${it.name}, skipping.", e)
                null
            }
        }
    }

    fun loadScript(script: String): SparkledScript<*> = try {
        scripts.getOrPut(script) {
            val (value, time) = measureTimedValue {
                val wrappedScript = """
                io.sparkled.renderer.api.scripting.SparkledScript<Any> { ctx -> 
                    with (ctx) {
                        $script
                    }
                } 
            """.trimIndent()
                scriptEngine.eval(wrappedScript) as SparkledScript<*>
            }

            logger.info("Compiled script.", "duration" to time.inWholeMilliseconds, "script" to script)
            value
        }
    } catch (e: Exception) {
        logger.error("Failed to compile script $script.", e)
        throw e
    }

    private val scriptEngine get() = ScriptEngineManager().getEngineByExtension("kts")

    companion object {
        private val logger = getLogger<SparkledPluginManager>()
    }
}
