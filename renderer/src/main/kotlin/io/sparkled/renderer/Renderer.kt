package io.sparkled.renderer

import io.sparkled.model.animation.ChannelPropPair
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.animation.effect.EffectTypeCode
import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.model.entity.StageProp
import io.sparkled.model.render.Led
import io.sparkled.model.render.RenderedFrame
import io.sparkled.model.render.RenderedStagePropData
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.renderer.effect.EffectRenderer
import io.sparkled.renderer.util.ChannelPropPairUtils
import io.sparkled.renderer.util.EffectTypeRenderers
import java.util.UUID

class Renderer(private val sequence: Sequence, sequenceChannels: List<SequenceChannel>, stageProps: List<StageProp>, private val startFrame: Int, private val endFrame: Int) {
    private val channelPropPairs: List<ChannelPropPair>

    init {
        this.channelPropPairs = ChannelPropPairUtils.makePairs(sequenceChannels, stageProps)
    }

    fun render(): RenderedStagePropDataMap {
        val renderedProps = RenderedStagePropDataMap()

        channelPropPairs.forEach { cpp ->
            val stagePropUuid = cpp.getStageProp().getUuid()
            val data = renderedProps.get(stagePropUuid)
            renderedProps.put(stagePropUuid, renderChannel(cpp, data))
        }
        return renderedProps
    }

    private fun renderChannel(channelPropPair: ChannelPropPair, renderedStagePropData: RenderedStagePropData?): RenderedStagePropData {
        var renderedStagePropData = renderedStagePropData
        if (renderedStagePropData == null) {
            val frameCount = endFrame - startFrame + 1
            val leds = channelPropPair.getStageProp().getLedCount()
            val data = ByteArray(frameCount * leds * Led.BYTES_PER_LED)
            renderedStagePropData = RenderedStagePropData(startFrame, endFrame, leds, data)
        }

        val dataToRender = renderedStagePropData
        channelPropPair.getChannel().getEffects().forEach({ effect -> renderEffect(sequence, dataToRender, effect) })

        return renderedStagePropData
    }

    private fun renderEffect(sequence: Sequence, renderedStagePropData: RenderedStagePropData, effect: Effect) {
        val effectTypeCode = effect.getType()
        val renderer = EffectTypeRenderers.get(effectTypeCode)

        val startFrame = Math.max(this.startFrame, effect.getStartFrame())
        val endFrame = Math.min(this.endFrame, effect.getEndFrame())

        for (frameNumber in startFrame..endFrame) {
            val frame = renderedStagePropData.getFrames().get(frameNumber - this.startFrame)
            renderer.render(sequence, renderedStagePropData, frame, effect)
        }
    }
}
