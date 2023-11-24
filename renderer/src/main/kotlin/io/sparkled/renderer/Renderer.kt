package io.sparkled.renderer

import common.logging.getLogger
import io.sparkled.model.embedded.ChannelData
import io.sparkled.model.SequenceChannelModel
import io.sparkled.model.SequenceModel
import io.sparkled.model.StageModel
import io.sparkled.model.StagePropModel
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.render.Led
import io.sparkled.model.render.RenderResult
import io.sparkled.model.render.RenderedFrame
import io.sparkled.model.render.RenderedStagePropData
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.StatefulSparkledEffect
import io.sparkled.renderer.easing.function.LinearEasing
import java.awt.image.BufferedImage
import kotlin.math.max
import kotlin.math.min

// TODO inject the renderer, so cache can be injected.
class Renderer(
    private val pluginManager: SparkledPluginManager,
    private val gifs: Map<String, List<BufferedImage>>,
    private val stage: StageModel,
    private val sequence: SequenceModel,
    sequenceChannels: List<SequenceChannelModel>,
    private val stageProps: List<StagePropModel>,
    private val startFrame: Int,
    private val endFrame: Int,
    private val preview: Boolean,
) {
    private val channelPropPairs = sequenceChannels.map {
        it.channelData to stageProps.first { sp -> sp.id == it.stagePropId }
    }

    fun render(): RenderResult {
        val renderedProps = RenderedStagePropDataMap()

        val groupedStageProps = if (preview) {
            stageProps.groupBy { it.id }
        } else {
            stageProps
                .sortedBy { it.groupDisplayOrder }
                .groupBy { it.groupCode ?: it.id }
        }

        groupedStageProps.forEach { (groupCode, stageProps) ->
            val frameCount = endFrame - startFrame + 1
            val leds = stageProps.sumOf { it.ledCount }
            val buffer = ByteArray(frameCount * leds * Led.BYTES_PER_LED)

            var nextStartIndex = 0
            val ledIndexes = stageProps.associate {
                val startIndex = nextStartIndex
                nextStartIndex += it.ledCount
                it.id to (startIndex until startIndex + it.ledCount)
            }

            renderedProps[groupCode] = RenderedStagePropData(startFrame, endFrame, leds, buffer, ledIndexes)
        }

        channelPropPairs.reversed().forEach { (channelData, stageProp) ->
            val groupCode = if (preview) stageProp.id else {
                stageProp.groupCode ?: stageProp.id
            }

            val data = renderedProps[groupCode]!!
            renderChannel(stage, channelData, stageProp, data)
        }

        return RenderResult(renderedProps, startFrame, endFrame - startFrame + 1)
    }

    private fun renderChannel(
        stage: StageModel,
        channelData: ChannelData,
        stageProp: StagePropModel,
        data: RenderedStagePropData,
    ): RenderedStagePropData {
        channelData.forEach {
            renderEffect(stage, sequence, data, stageProp, it)
        }

        return data
    }

    private fun renderEffect(
        stage: StageModel,
        sequence: SequenceModel,
        data: RenderedStagePropData,
        prop: StagePropModel,
        effect: Effect,
    ) {
        repeat(effect.repetitions) {
            val duration = effect.endFrame - effect.startFrame + 1

            val spacedDuration = duration + effect.repetitionSpacing
            val newStartFrame = effect.startFrame + (spacedDuration * it)

            val effectRepetition = effect.copy(startFrame = newStartFrame, endFrame = newStartFrame + duration - 1)
            renderRepetition<Any>(stage, sequence, data, prop, effectRepetition)
        }
    }

    private fun <T> renderRepetition(
        stage: StageModel,
        sequence: SequenceModel,
        data: RenderedStagePropData,
        prop: StagePropModel,
        effect: Effect,
    ) {
        val effectTypeCode = effect.type

        @Suppress("UNCHECKED_CAST")
        val effectRenderer = pluginManager.effects.get()[effectTypeCode] as StatefulSparkledEffect<T>?

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

            val firstFrame = data.frames[startFrame - this.startFrame]
            val state = effectRenderer.createState(
                RenderContext(
                    stage,
                    sequence,
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
            if (state != null) {
                for (frameNumber in effect.startFrame until startFrame) {
                    val frame = RenderedFrame(
                        startFrame = effect.startFrame,
                        frameNumber = frameNumber,
                        ledCount = firstFrame.ledCount,
                        data = byteArrayOf(),
                        dummyFrame = true,
                    )
                    render(stage, sequence, data, frame, prop, effect, effectRenderer, state)
                }
            }

            for (frameNumber in startFrame..endFrame) {
                val frame = data.frames[frameNumber - this.startFrame]
                render(stage, sequence, data, frame, prop, effect, effectRenderer, state)
            }
        }
    }

    private fun <T> render(
        stage: StageModel,
        sequence: SequenceModel,
        channel: RenderedStagePropData,
        frame: RenderedFrame,
        stageProp: StagePropModel,
        effect: Effect,
        renderer: StatefulSparkledEffect<T>,
        state: T,
    ) {
        val progress = getProgress(frame, effect)
        val ctx = RenderContext(
            stage,
            sequence,
            channel,
            frame,
            stageProp,
            effect,
            progress,
            pluginManager.fills.get(),
            this::loadGif,
        )
        renderer.render(ctx, state)
    }

    private fun loadGif(filename: String): List<BufferedImage> {
        return gifs[filename] ?: emptyList()
    }

    private fun getProgress(frame: RenderedFrame, effect: Effect): Float {
        val easingFunction = pluginManager.easings.get()[effect.easing.type] ?: LinearEasing

        val currentFrame = frame.frameNumber - effect.startFrame
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
