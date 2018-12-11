package io.sparkled.renderer

import io.sparkled.model.animation.ChannelPropPair
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.model.entity.StageProp
import io.sparkled.model.render.Led
import io.sparkled.model.render.RenderResult
import io.sparkled.model.render.RenderedStagePropData
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.renderer.util.ChannelPropPairUtils
import io.sparkled.renderer.util.EffectTypeRenderers

class Renderer(
    private val sequence: Sequence,
    sequenceChannels: List<SequenceChannel>,
    stageProps: List<StageProp>,
    private val startFrame: Int,
    private val endFrame: Int
) {
    private val channelPropPairs: List<ChannelPropPair> = ChannelPropPairUtils.makePairs(sequenceChannels, stageProps)

    fun render(): RenderResult {
        val renderedProps = RenderedStagePropDataMap()

        channelPropPairs.forEach { cpp ->
            val stagePropUuid = cpp.stageProp.getUuid()!!
            val data = renderedProps[stagePropUuid]
            renderedProps[stagePropUuid] = renderChannel(cpp, data)
        }

        return RenderResult(renderedProps, endFrame - startFrame + 1)
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
            val newStartFrame = effect.startFrame + (duration * it)

            val effectRepetition = effect.copy(startFrame = newStartFrame, endFrame = newStartFrame + duration - 1)
            renderRepetition(sequence, data, prop, effectRepetition)
        }
    }

    private fun renderRepetition(sequence: Sequence, data: RenderedStagePropData, prop: StageProp, effect: Effect) {
        val effectTypeCode = effect.type
        val renderer = EffectTypeRenderers[effectTypeCode]

        val startFrame = Math.max(this.startFrame, effect.startFrame)
        val endFrame = Math.min(this.endFrame, effect.endFrame)

        for (frameNumber in startFrame..endFrame) {
            val frame = data.frames[frameNumber - this.startFrame]
            renderer.render(sequence, data, frame, prop, effect)
        }
    }
}
