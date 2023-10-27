package io.sparkled.renderer.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.sparkled.model.animation.ChannelPropPair
import io.sparkled.model.animation.SequenceChannelEffects

/**
 * Pairs up sequence channels with their associated stage prop.
 */
object ChannelPropPairUtils {

    fun makePairs(objectMapper: ObjectMapper, sequenceChannels: List<SequenceChannelModel>, stageProps: List<StagePropModel>): List<ChannelPropPair> {
        return sequenceChannels.map { sc -> getPair(objectMapper, sc, stageProps) }
    }

    private fun getPair(objectMapper: ObjectMapper, sequenceChannel: SequenceChannelModel, stageProps: List<StagePropModel>): ChannelPropPair {
        return ChannelPropPair(
            convertChannelData(objectMapper, sequenceChannel),
            stageProps.first { sp -> sp.id == sequenceChannel.stagePropId }
        )
    }

    private fun convertChannelData(objectMapper: ObjectMapper, sequenceChannel: SequenceChannelModel): SequenceChannelEffects {
        return SequenceChannelEffects(
            effects = objectMapper.readValue(sequenceChannel.channelJson)
        )
    }
}
