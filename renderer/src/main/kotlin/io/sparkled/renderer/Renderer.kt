package io.sparkled.renderer

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.model.animation.ChannelPropPair
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.model.entity.StageProp
import io.sparkled.model.render.*
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SparkledEffect
import io.sparkled.renderer.easing.function.LinearEasing
import io.sparkled.renderer.util.ChannelPropPairUtils
import org.slf4j.LoggerFactory
import kotlin.math.max
import kotlin.math.min

// TODO inject the renderer, so cache and objectMapper can be injected.
class Renderer(
    private val pluginManager: SparkledPluginManager,
    objectMapper: ObjectMapper,
    private val sequence: Sequence,
    sequenceChannels: List<SequenceChannel>,
    stageProps: List<StageProp>,
    private val startFrame: Int,
    private val endFrame: Int
) {
    private val channelPropPairs: List<ChannelPropPair> = ChannelPropPairUtils.makePairs(objectMapper, sequenceChannels, stageProps)

    fun render(): RenderResult {
        val renderedProps = RenderedStagePropDataMap()

        // Channels are rendered in reverse order for blending purposes.
        channelPropPairs.reversed().forEach { cpp ->
            val stagePropUuid = cpp.stageProp.getUuid()!!
            val data = renderedProps[stagePropUuid]
            renderedProps[stagePropUuid] = renderChannel(cpp, data)
        }

        return RenderResult(renderedProps, startFrame, endFrame - startFrame + 1)
    }

    private fun renderChannel(channelPropPair: ChannelPropPair, data: RenderedStagePropData?): RenderedStagePropData {
        val stagePropData = if (data != null) {
            data
        } else {
            val frameCount = endFrame - startFrame + 1
            val leds = channelPropPair.stageProp.getLedCount()!!
            val buffer = ByteArray(frameCount * leds * Led.BYTES_PER_LED)
            RenderedStagePropData(startFrame, endFrame, leds, buffer)
        }

        channelPropPair.channel.effects.forEach {
            renderEffect(sequence, stagePropData, channelPropPair.stageProp, it)
        }

        return stagePropData
    }

    private fun renderEffect(sequence: Sequence, data: RenderedStagePropData, prop: StageProp, effect: Effect) {
        repeat(effect.repetitions) {
            val duration = effect.endFrame - effect.startFrame + 1

            val spacedDuration = duration + effect.repetitionSpacing
            val newStartFrame = effect.startFrame + (spacedDuration * it)

            val effectRepetition = effect.copy(startFrame = newStartFrame, endFrame = newStartFrame + duration - 1)
            renderRepetition<Any>(sequence, data, prop, effectRepetition)
        }
    }

    private fun <T> renderRepetition(sequence: Sequence, data: RenderedStagePropData, prop: StageProp, effect: Effect) {
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
                RenderContext(sequence, data, firstFrame, prop, effect, 0f, pluginManager.fills.get())
            )

            // Stateful effects need to be rendered from the beginning, so perform a dummy render on any frames that
            // fall outside of the preview window.
            if (state != null) {
                for (frameNumber in effect.startFrame until startFrame) {
                    val frame = RenderedFrame(startFrame = effect.startFrame, frameNumber = frameNumber, ledCount = firstFrame.ledCount, data = byteArrayOf(), dummyFrame = true)
                    render(sequence, data, frame, prop, effect, effectRenderer, state)
                }
            }
            
            for (frameNumber in startFrame..endFrame) {
                val frame = data.frames[frameNumber - this.startFrame]
                render(sequence, data, frame, prop, effect, effectRenderer, state)
            }
        }
    }

    fun <T> render(sequence: Sequence, channel: RenderedStagePropData, frame: RenderedFrame, stageProp: StageProp, effect: Effect, renderer: SparkledEffect<T>, state: T) {
        val progress = getProgress(frame, effect)
        val ctx = RenderContext(sequence, channel, frame, stageProp, effect, progress, pluginManager.fills.get())
        renderer.render(ctx, state)
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
