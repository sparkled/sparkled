package io.sparkled.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.sparkled.model.SequenceChannelModel
import io.sparkled.model.SequenceModel
import io.sparkled.model.StageModel
import io.sparkled.model.StagePropModel
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.config.SparkledConfig
import io.sparkled.model.enumeration.SequenceStatus
import io.sparkled.model.render.RenderedStagePropData
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.renderer.Renderer
import io.sparkled.renderer.SparkledPluginManager
import java.util.*

object RenderUtils {

    private val objectMapper = jacksonObjectMapper()
    private val pluginManager = SparkledPluginManager(
        SparkledConfig(
            dataFolderPath = ".",
            audioFolderName = ".",
            gifFolderName = ".",
            pluginFolderName = ".",
            renderFolderName = ".",
        )
    ).apply { reloadPlugins() }

    const val PROP_ID = "0"
    const val PROP_CODE = "TEST_PROP"

    fun render(effect: Effect, frameCount: Int, ledCount: Int): RenderedStagePropData {
        val stageProp = StagePropModel(stageId = "0", code = PROP_CODE, id = PROP_ID, ledCount = ledCount, reverse = false)
        val result = render(mapOf(PROP_ID to listOf(effect)), frameCount, listOf(stageProp))
        return result.getValue(PROP_ID)
    }

    fun render(
        effects: Map<UUID, List<Effect>>,
        frameCount: Int,
        stageProps: List<StagePropModel>
    ): RenderedStagePropDataMap {
        val stage = StageModel(name = "Test stage", width = 800, height = 600)
        val sequence =
            SequenceModel(framesPerSecond = 60, stageId = 0, status = SequenceStatus.DRAFT, name = "Test", songId = 1)
        val sequenceChannels = effects.map {
            SequenceChannelModel(
                stagePropId = it.key,
                channelJson = objectMapper.writeValueAsString(it.value),
                name = "Test",
                sequenceId = 1,
                displayOrder = 1,
            )
        }

        val renderResult = Renderer(
            pluginManager,
            emptyMap(),
            objectMapper,
            stage,
            sequence,
            sequenceChannels,
            stageProps,
            0,
            frameCount - 1,
            preview = false,
        ).render()

        return renderResult.stageProps
    }
}
