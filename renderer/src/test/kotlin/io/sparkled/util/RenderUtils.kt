package io.sparkled.util

import io.sparkled.model.embedded.ChannelData
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
import io.sparkled.renderer.RenderMode
import io.sparkled.renderer.Renderer
import io.sparkled.renderer.SparkledPluginManager

object RenderUtils {

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
        return result.getValue(stageProp.code)
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
                channelData = ChannelData.of(it.value),
                name = "Test",
            )
        }

        val renderResult = Renderer(
            pluginManager,
            { linkedMapOf() },
            stage,
            30,
            sequenceChannels.associate { it.stagePropId to it.channelData },
            stageProps.associateBy { it.id },
            0,
            frameCount - 1,
            mode = RenderMode.PUBLISH_SEQUENCE,
        ).render()

        return renderResult.stageProps
    }
}
