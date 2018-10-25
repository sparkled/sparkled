package io.sparkled.util

import io.sparkled.model.animation.SequenceChannelEffects
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.model.entity.StageProp
import io.sparkled.model.render.RenderedStagePropData
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.model.util.GsonProvider
import io.sparkled.renderer.Renderer

import java.util.Collections
import java.util.UUID

object RenderUtils {

    private val PROP_UUID = UUID(0, 0)
    private val PROP_CODE = "TEST_PROP"

    fun render(effect: Effect, frameCount: Int, ledCount: Int): RenderedStagePropData {
        effect.setStartFrame(0)
        effect.setEndFrame(frameCount - 1)

        val animationData = SequenceChannelEffects()
        animationData.getEffects().add(effect)

        val sequence = Sequence().setFramesPerSecond(60)
        val channelJson = GsonProvider.get().toJson(animationData)
        val sequenceChannel = SequenceChannel().setStagePropUuid(PROP_UUID).setChannelJson(channelJson)

        val stageProp = StageProp().setCode(PROP_CODE).setUuid(PROP_UUID).setLedCount(ledCount)

        val renderedChannels = Renderer(
                sequence,
                Collections.singletonList(sequenceChannel),
                Collections.singletonList(stageProp),
                0,
                effect.getEndFrame()).render()
        return renderedChannels.get(PROP_UUID)
    }
}
