package io.sparkled.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.sparkled.model.StageModel
import io.sparkled.model.StagePropModel
import io.sparkled.model.UniqueId
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.config.SparkledConfig
import io.sparkled.model.render.RenderedStagePropData
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.model.util.testSequence
import io.sparkled.model.util.testSequenceChannel
import io.sparkled.model.util.testStageProp
import io.sparkled.renderer.Renderer
import io.sparkled.renderer.SparkledPluginManager

object RenderUtils {

    private val objectMapper = jacksonObjectMapper()

    private val pluginManager = SparkledPluginManager(
        SparkledConfig(
            dataFolderPath = ".",
            audioFolderName = ".",
            gifFolderName = ".",
            pluginFolderName = ".",
            renderFolderName = ".",
            clientUdpPort = 1,
            udpReceiveBufferSize = 1,
            udpSendBufferSize = 1,
        )
    ).apply { reloadPlugins() }

    fun render(effect: Effect, frameCount: Int, ledCount: Int): RenderedStagePropData {
        val stageProp = testStageProp.copy(ledCount = ledCount)
        val result = render(mapOf(stageProp.id to listOf(effect)), frameCount, listOf(stageProp))
        return result.getValue(stageProp.id)
    }

    fun render(
        effects: Map<UniqueId, List<Effect>>,
        frameCount: Int,
        stageProps: List<StagePropModel>,
    ): RenderedStagePropDataMap {
        val stage = StageModel(name = "Test stage", width = 800, height = 600)

        val sequenceChannels = effects.map {
            testSequenceChannel.copy(
                stagePropId = it.key,
                channelJson = objectMapper.writeValueAsString(it.value),
                name = "Test",
            )
        }

        val renderResult = Renderer(
            pluginManager,
            emptyMap(),
            objectMapper,
            stage,
            testSequence,
            sequenceChannels,
            stageProps,
            0,
            frameCount - 1,
            preview = false,
        ).render()

        return renderResult.stageProps
    }
}
