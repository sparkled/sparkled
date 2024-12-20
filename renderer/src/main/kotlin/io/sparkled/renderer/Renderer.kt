package io.sparkled.renderer

import io.sparkled.common.logging.getLogger
import io.sparkled.model.StageModel
import io.sparkled.model.StagePropModel
import io.sparkled.model.UniqueId
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.render.Led
import io.sparkled.model.render.RenderResult
import io.sparkled.model.render.RenderedFrame
import io.sparkled.model.render.RenderedStagePropData
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SparkledEffect
import io.sparkled.renderer.easing.function.LinearEasing
import java.awt.image.BufferedImage
import kotlin.math.max
import kotlin.math.min

enum class RenderMode {
    LIVE_FRAME,
    PREVIEW_SEQUENCE,
    PUBLISH_SEQUENCE,
}

// TODO inject the renderer, so cache can be injected.
class Renderer(
    private val pluginManager: SparkledPluginManager,
    private val gifs: () -> LinkedHashMap<String, List<BufferedImage>>,
    private val stage: StageModel,
    private val framesPerSecond: Int,
    private val stagePropEffects: Map<UniqueId, List<Effect>>,
    private val stageProps: Map<UniqueId, StagePropModel>,
    private val startFrame: Int,
    private val endFrame: Int,
    private val mode: RenderMode,
) {
    fun render(): RenderResult {
        val renderedProps = RenderedStagePropDataMap()

        val groupedStageProps = if (mode == RenderMode.PREVIEW_SEQUENCE) {
            stageProps.values.groupBy { it.code }
        } else {
            stageProps.values
                .sortedBy { it.groupDisplayOrder }
                .groupBy { if (it.groupCode.isNullOrBlank()) it.code else it.groupCode!! }
        }

        groupedStageProps.forEach { (groupCode, stageProps) ->
            val frameCount = endFrame - startFrame + 1
            val leds = stageProps.sumOf { it.ledCount }
            val buffer = ByteArray(frameCount * leds * Led.BYTES_PER_LED)

            var nextStartIndex = 0
            val pixelIndexes = stageProps.associate {
                val startIndex = nextStartIndex
                nextStartIndex += it.ledCount
                it.id to (startIndex until startIndex + it.ledCount)
            }

            renderedProps[groupCode] = RenderedStagePropData(startFrame, endFrame, leds, buffer, pixelIndexes)
        }

        stagePropEffects
            .map { it.value to stageProps[it.key]!! }
            .reversed()
            .forEach { (channelData, stageProp) ->
                val groupCode = if (mode == RenderMode.PREVIEW_SEQUENCE) stageProp.code else {
                    if (stageProp.groupCode.isNullOrBlank()) stageProp.code else stageProp.groupCode!!
                }

                val data = renderedProps[groupCode]!!
                renderChannel(channelData, stageProp, data)
            }

        return RenderResult(renderedProps, startFrame, endFrame - startFrame + 1)
    }

    private fun renderChannel(
        channelData: List<Effect>,
        stageProp: StagePropModel,
        data: RenderedStagePropData,
    ): RenderedStagePropData {
        channelData.forEach {
            renderEffect(data, stageProp, it)
        }

        return data
    }

    private fun renderEffect(
        data: RenderedStagePropData,
        prop: StagePropModel,
        effect: Effect,
    ) {
        repeat(effect.repetitions) {
            val duration = effect.endFrame - effect.startFrame + 1

            val spacedDuration = duration + effect.repetitionSpacing
            val newStartFrame = effect.startFrame + (spacedDuration * it)

            val effectRepetition = effect.copy(startFrame = newStartFrame, endFrame = newStartFrame + duration - 1)
            renderRepetition<Any>(data, prop, effectRepetition)
        }
    }

    private fun <T> renderRepetition(
        data: RenderedStagePropData,
        prop: StagePropModel,
        effect: Effect,
    ) {
        val effectTypeCode = effect.type

        @Suppress("UNCHECKED_CAST")
        val effectRenderer = pluginManager.effects.get()[effectTypeCode] as SparkledEffect<T>?

        if (effectRenderer == null) {
            logger.warn("Failed to find effect '${effect.type}, skipping.")
        } else {
            if (effect.startFrame >= endFrame || effect.endFrame <= startFrame) {
                // Don't render out-of-bounds effects.
                return
            }

            val startFrame = max(this.startFrame, effect.startFrame)
            val endFrame = min(this.endFrame, effect.endFrame)

            if (startFrame - this.startFrame >= data.frames.size) {
                return
            }

            val firstFrame = when (mode) {
                RenderMode.LIVE_FRAME -> data.frames[0]
                else -> data.frames[startFrame - this.startFrame]
            }

            val state = effectRenderer.createState(
                RenderContext(
                    pluginManager,
                    stage,
                    framesPerSecond,
                    data,
                    firstFrame,
                    prop,
                    effect,
                    0f,
                    pluginManager.fills.get(),
                    this::loadGif,
                ),
            )

            // Stateful effects need to be rendered from the beginning, so perform a dummy render on any frames that
            // fall outside the preview window.
            if (state != Unit) {
                for (frameIndex in effect.startFrame..<startFrame) {
                    val frame = RenderedFrame(
                        startFrame = effect.startFrame,
                        frameIndex = frameIndex,
                        ledCount = firstFrame.ledCount,
                        data = byteArrayOf(),
                        dummyFrame = true,
                    )
                    render(framesPerSecond, data, frame, prop, effect, effectRenderer, state)
                }
            }

            for (frameIndex in startFrame..endFrame) {
                val frame = when (mode) {
                    RenderMode.LIVE_FRAME -> data.frames[0]
                    else -> data.frames[frameIndex - this.startFrame]
                }

                render(framesPerSecond, data, frame, prop, effect, effectRenderer, state)
            }
        }
    }

    private fun <T> render(
        framesPerSecond: Int,
        channel: RenderedStagePropData,
        frame: RenderedFrame,
        stageProp: StagePropModel,
        effect: Effect,
        effectRenderer: SparkledEffect<T>,
        state: T & Any,
    ) {
        val progress = getProgress(frame.frameIndex, effect)
        val ctx = RenderContext(
            pluginManager,
            stage,
            framesPerSecond,
            channel,
            frame,
            stageProp,
            effect,
            progress,
            pluginManager.fills.get(),
            this::loadGif,
        )
        effectRenderer.render(ctx, state)
    }

    private fun loadGif(filename: String): List<BufferedImage> {
        return gifs()[filename] ?: emptyList()
    }

    private fun getProgress(frameIndex: Int, effect: Effect): Float {
        val easingFunction = pluginManager.easings.get()[effect.easing.type] ?: LinearEasing

        val currentFrame = frameIndex - effect.startFrame
        val startFrame = effect.startFrame
        val duration = effect.endFrame - startFrame + 1

        val progress = easingFunction.getScaledProgress(effect.easing, currentFrame, duration)
        if (progress < 0 || progress > 1) {
            throw IllegalStateException("Animation progress is out of bounds: $progress")
        }

        return progress
    }

    companion object {
        private val logger = getLogger<Renderer>()
    }
}
