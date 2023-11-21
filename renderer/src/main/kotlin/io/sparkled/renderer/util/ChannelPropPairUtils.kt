package io.sparkled.renderer.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.sparkled.model.SequenceChannelModel
import io.sparkled.model.StagePropModel
import io.sparkled.model.animation.ChannelPropPair
import io.sparkled.model.animation.SequenceChannelEffects

/**
 * Pairs up sequence channels with their associated stage prop.
 */
object ChannelPropPairUtils {

    fun makePairs(
        objectMapper: ObjectMapper,
        sequenceChannels: List<SequenceChannelModel>,
        stageProps: List<StagePropModel>
    ) = sequenceChannels.map { getPair(objectMapper, it, stageProps) }

    private fun getPair(
        objectMapper: ObjectMapper,
        sequenceChannel: SequenceChannelModel,
        stageProps: List<StagePropModel>,
    ): ChannelPropPair {
        return ChannelPropPair(
            convertChannelData(objectMapper, sequenceChannel),
            stageProps.first { sp -> sp.id == sequenceChannel.stagePropId }
        )
    }

    private fun convertChannelData(
        objectMapper: ObjectMapper,
        sequenceChannel: SequenceChannelModel,
    ): SequenceChannelEffects {
        return SequenceChannelEffects(
            effects = objectMapper.readValue(sequenceChannel.channelJson)
        )
    }
}
