package io.sparkled.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.config.SparkledConfig
import io.sparkled.model.enumeration.SequenceStatus
import io.sparkled.model.entity.v2.SequenceChannelEntity
import io.sparkled.model.entity.v2.SequenceEntity
import io.sparkled.model.entity.v2.StageEntity
import io.sparkled.model.entity.v2.StagePropEntity
import io.sparkled.model.render.RenderedStagePropData
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.renderer.Renderer
import io.sparkled.renderer.SparkledPluginManager
import java.util.*

object RenderUtils {

    private val objectMapper = ObjectMapper().registerKotlinModule()
    private val pluginManager = SparkledPluginManager(
        SparkledConfig(
            dataFolderPath = ".",
            audioFolderName = ".",
            gifFolderName = ".",
            pluginFolderName = ".",
            renderFolderName = ".",
        )
    ).apply { reloadPlugins() }

    val PROP_UUID = UUID(0, 0)
    const val PROP_CODE = "TEST_PROP"

    fun render(effect: Effect, frameCount: Int, ledCount: Int): RenderedStagePropData {
        val stageProp = StagePropEntity(code = PROP_CODE, uuid = PROP_UUID, ledCount = ledCount, reverse = false)
        val result = render(mapOf(PROP_UUID to listOf(effect)), frameCount, listOf(stageProp))
        return result.getValue(PROP_UUID.toString())
    }

    fun render(
        effects: Map<UUID, List<Effect>>,
        frameCount: Int,
        stageProps: List<StagePropEntity>
    ): RenderedStagePropDataMap {
        val stage = StageEntity(name = "Test stage", width = 800, height = 600)
        val sequence =
            SequenceEntity(framesPerSecond = 60, stageId = 0, status = SequenceStatus.DRAFT, name = "Test", songId = 1)
        val sequenceChannels = effects.map {
            SequenceChannelEntity(
                stagePropUuid = it.key,
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
            preview = false
        ).render()

        return renderResult.stageProps
    }
}
