package io.sparkled.renderer

import io.sparkled.model.animation.ChannelPropPair
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.model.entity.StageProp
import io.sparkled.model.render.Led
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

    fun render(): RenderedStagePropDataMap {
        val renderedProps = RenderedStagePropDataMap()

        channelPropPairs.forEach { cpp ->
            val stagePropUuid = cpp.stageProp.getUuid()!!
            val data = renderedProps[stagePropUuid]
            renderedProps[stagePropUuid] = renderChannel(cpp, data)
        }
        return renderedProps
    }

    private fun renderChannel(
        channelPropPair: ChannelPropPair,
        renderedStagePropData: RenderedStagePropData?
    ): RenderedStagePropData {
        var stagePropData = renderedStagePropData
        if (stagePropData == null) {
            val frameCount = endFrame - startFrame + 1
            val leds = channelPropPair.stageProp.getLedCount()!!
            val data = ByteArray(frameCount * leds * Led.BYTES_PER_LED)
            stagePropData = RenderedStagePropData(startFrame, endFrame, leds, data)
        }

        val dataToRender = stagePropData
        channelPropPair.channel.getEffects().forEach { renderEffect(sequence, dataToRender, it) }

        return stagePropData
    }

    private fun renderEffect(sequence: Sequence, renderedStagePropData: RenderedStagePropData, effect: Effect) {
        val effectTypeCode = effect.getType()!!
        val renderer = EffectTypeRenderers[effectTypeCode]

        val startFrame = Math.max(this.startFrame, effect.getStartFrame())
        val endFrame = Math.min(this.endFrame, effect.getEndFrame())

        for (frameNumber in startFrame..endFrame) {
            val frame = renderedStagePropData.frames[frameNumber - this.startFrame]
            renderer.render(sequence, renderedStagePropData, frame, effect)
        }
    }
}
