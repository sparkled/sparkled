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
import io.sparkled.renderer.Renderer
import io.sparkled.renderer.SparkledPluginManager
import java.util.UUID

object RenderUtils {
    
    private val objectMapper = ObjectMapper().registerKotlinModule()
    private val pluginManager = SparkledPluginManager(SparkledConfig(directory = ".")).apply { reloadPlugins() }
    val PROP_UUID = UUID(0, 0)
    const val PROP_CODE = "TEST_PROP"

    fun render(effect: Effect, frameCount: Int, ledCount: Int): RenderedStagePropData {
        val stageProp = StageProp().setCode(PROP_CODE).setUuid(PROP_UUID).setLedCount(ledCount).setReverse(false)
        return render(effect, frameCount, stageProp)
    }

    fun render(effect: Effect, frameCount: Int, stageProp: StageProp): RenderedStagePropData {
        val animationData = SequenceChannelEffects(listOf(effect))

        val sequence = Sequence().setFramesPerSecond(60)
        val channelJson = objectMapper.writeValueAsString(animationData)
        val sequenceChannel = SequenceChannel().setStagePropUuid(PROP_UUID).setChannelJson(channelJson)

        val renderResult = Renderer(
            pluginManager,
            objectMapper,
            sequence,
            listOf(sequenceChannel),
            listOf(stageProp),
            0,
            frameCount - 1
        ).render()

        return renderResult.stageProps[PROP_UUID]!!
    }
}
