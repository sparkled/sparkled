package io.sparkled.renderer

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.model.animation.ChannelPropPair
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.entity.v2.SequenceChannelEntity
import io.sparkled.model.entity.v2.SequenceEntity
import io.sparkled.model.entity.v2.StageEntity
import io.sparkled.model.entity.v2.StagePropEntity
import io.sparkled.model.render.*
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SparkledEffect
import io.sparkled.renderer.easing.function.LinearEasing
import io.sparkled.renderer.util.ChannelPropPairUtils
import org.slf4j.LoggerFactory
import java.awt.image.BufferedImage
import kotlin.math.max
import kotlin.math.min

// TODO inject the renderer, so cache and objectMapper can be injected.
class Renderer(
    private val pluginManager: SparkledPluginManager,
    private val gifs: Map<String, List<BufferedImage>>,
    objectMapper: ObjectMapper,
    private val stage: StageEntity,
    private val sequence: SequenceEntity,
    sequenceChannels: List<SequenceChannelEntity>,
    stageProps: List<StagePropEntity>,
    private val startFrame: Int,
    private val endFrame: Int,
    // TODO there was a preview flag here. Where did that go?
) {
    private val channelPropPairs: List<ChannelPropPair> = ChannelPropPairUtils.makePairs(objectMapper, sequenceChannels, stageProps)

    fun render(): RenderResult {
        val renderedProps = RenderedStagePropDataMap()

        // Channels are rendered in reverse order for blending purposes.
        channelPropPairs.reversed().forEach { cpp ->
            val stagePropUuid = cpp.stageProp.uuid
            val data = renderedProps[stagePropUuid]
            renderedProps[stagePropUuid] = renderChannel(stage, cpp, data)
        }

        return RenderResult(renderedProps, startFrame, endFrame - startFrame + 1)
    }

    private fun renderChannel(stage: StageEntity, channelPropPair: ChannelPropPair, data: RenderedStagePropData?): RenderedStagePropData {
        val stagePropData = if (data != null) {
            data
        } else {
            val frameCount = endFrame - startFrame + 1
            val leds = channelPropPair.stageProp.ledCount
            val buffer = ByteArray(frameCount * leds * Led.BYTES_PER_LED)
            RenderedStagePropData(startFrame, endFrame, leds, buffer)
        }

        channelPropPair.channel.effects.forEach {
            renderEffect(stage, sequence, stagePropData, channelPropPair.stageProp, it)
        }

        return stagePropData
    }

    private fun renderEffect(stage: StageEntity, sequence: SequenceEntity, data: RenderedStagePropData, prop: StagePropEntity, effect: Effect) {
        repeat(effect.repetitions) {
            val duration = effect.endFrame - effect.startFrame + 1

            val spacedDuration = duration + effect.repetitionSpacing
            val newStartFrame = effect.startFrame + (spacedDuration * it)

            val effectRepetition = effect.copy(startFrame = newStartFrame, endFrame = newStartFrame + duration - 1)
            renderRepetition<Any>(stage, sequence, data, prop, effectRepetition)
        }
    }

    private fun <T> renderRepetition(stage: StageEntity, sequence: SequenceEntity, data: RenderedStagePropData, prop: StagePropEntity, effect: Effect) {
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

            val firstFrame = data.frames[startFrame - this.startFrame]
            val state = effectRenderer.createState(
                RenderContext(stage, sequence, data, firstFrame, prop, effect, 0f, pluginManager.fills.get(), this::loadGif)
            )

            // Stateful effects need to be rendered from the beginning, so perform a dummy render on any frames that
            // fall outside of the preview window.
            if (state != null) {
                for (frameNumber in effect.startFrame until startFrame) {
                    val frame = RenderedFrame(startFrame = effect.startFrame, frameNumber = frameNumber, ledCount = firstFrame.ledCount, data = byteArrayOf(), dummyFrame = true)
                    render(stage, sequence, data, frame, prop, effect, effectRenderer, state)
                }
            }

            for (frameNumber in startFrame..endFrame) {
                val frame = data.frames[frameNumber - this.startFrame]
                render(stage, sequence, data, frame, prop, effect, effectRenderer, state)
            }
        }
    }

    private fun <T> render(stage: StageEntity, sequence: SequenceEntity, channel: RenderedStagePropData, frame: RenderedFrame, stageProp: StagePropEntity, effect: Effect, renderer: SparkledEffect<T>, state: T) {
        val progress = getProgress(frame, effect)
        val ctx = RenderContext(stage, sequence, channel, frame, stageProp, effect, progress, pluginManager.fills.get(), this::loadGif)
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
        private val logger = LoggerFactory.getLogger(Renderer::class.java)
    }
}
