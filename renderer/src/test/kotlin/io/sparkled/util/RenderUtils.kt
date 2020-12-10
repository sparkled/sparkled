package io.sparkled.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.sparkled.model.animation.SequenceChannelEffects
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.config.SparkledConfig
import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.model.entity.StageProp
import io.sparkled.model.render.RenderedStagePropData
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.renderer.Renderer
import io.sparkled.renderer.SparkledPluginManager
import java.util.*

object RenderUtils {

    private val objectMapper = ObjectMapper().registerKotlinModule()
    private val pluginManager = SparkledPluginManager(SparkledConfig(directory = ".")).apply { reloadPlugins() }
    val PROP_UUID = UUID(0, 0)
    const val PROP_CODE = "TEST_PROP"

    fun render(effect: Effect, frameCount: Int, ledCount: Int): RenderedStagePropData {
        val stageProp = StageProp().setCode(PROP_CODE).setUuid(PROP_UUID).setLedCount(ledCount).setReverse(false)
        val result = render(mapOf(PROP_UUID to listOf(effect)), frameCount, listOf(stageProp))
        return result[PROP_UUID.toString()]!!
    }

    fun render(
        effects: Map<UUID, List<Effect>>,
        frameCount: Int,
        stageProps: List<StageProp>
    ): RenderedStagePropDataMap {
        val sequence = Sequence().setFramesPerSecond(60)
        val sequenceChannels = effects.map {
            SequenceChannel().apply {
                setStagePropUuid(it.key)
                setChannelJson(objectMapper.writeValueAsString(SequenceChannelEffects(it.value)))
            }
        }

        val renderResult = Renderer(
            pluginManager,
            objectMapper,
            sequence,
            sequenceChannels,
            stageProps,
            0,
            frameCount - 1,
            preview = false
        ).render()

        return renderResult.stageProps
    }
}
