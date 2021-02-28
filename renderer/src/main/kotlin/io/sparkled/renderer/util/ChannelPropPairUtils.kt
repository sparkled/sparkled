package io.sparkled.renderer.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.sparkled.model.animation.ChannelPropPair
import io.sparkled.model.animation.SequenceChannelEffects
import io.sparkled.model.entity.v2.SequenceChannelEntity
import io.sparkled.model.entity.v2.StagePropEntity

/**
 * Pairs up sequence channels with their associated stage prop.
 */
object ChannelPropPairUtils {

    fun makePairs(objectMapper: ObjectMapper, sequenceChannels: List<SequenceChannelEntity>, stageProps: List<StagePropEntity>): List<ChannelPropPair> {
        return sequenceChannels.map { sc -> getPair(objectMapper, sc, stageProps) }
    }

    private fun getPair(objectMapper: ObjectMapper, sequenceChannel: SequenceChannelEntity, stageProps: List<StagePropEntity>): ChannelPropPair {
        return ChannelPropPair(
            convertChannelData(objectMapper, sequenceChannel),
            stageProps.first { sp -> sp.uuid == sequenceChannel.stagePropUuid }
        )
    }

    private fun convertChannelData(objectMapper: ObjectMapper, sequenceChannel: SequenceChannelEntity): SequenceChannelEffects {
        return SequenceChannelEffects(
            effects = objectMapper.readValue(sequenceChannel.channelJson)
        )
    }
}
