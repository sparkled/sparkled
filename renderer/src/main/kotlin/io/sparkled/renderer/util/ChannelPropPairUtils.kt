package io.sparkled.renderer.util

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.model.animation.ChannelPropPair
import io.sparkled.model.animation.SequenceChannelEffects
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.model.entity.StageProp
import java.util.UUID

/**
 * Pairs up sequence channels with their associated stage prop.
 */
object ChannelPropPairUtils {

    fun makePairs(objectMapper: ObjectMapper, sequenceChannels: List<SequenceChannel>, stageProps: List<StageProp>): List<ChannelPropPair> {
        return sequenceChannels.asSequence().map { sc -> getPair(objectMapper, sc, stageProps) }.toList()
    }

    private fun getPair(objectMapper: ObjectMapper, sequenceChannel: SequenceChannel, stageProps: List<StageProp>): ChannelPropPair {
        return ChannelPropPair(
            convertChannelData(objectMapper, sequenceChannel),
            findStagePropByUuid(stageProps, sequenceChannel.getStagePropUuid()!!)
        )
    }

    private fun convertChannelData(objectMapper: ObjectMapper, sequenceChannel: SequenceChannel): SequenceChannelEffects {
        return objectMapper.readValue(sequenceChannel.getChannelJson(), SequenceChannelEffects::class.java)
    }

    private fun findStagePropByUuid(stageProps: List<StageProp>, uuid: UUID): StageProp {
        return stageProps
            .asSequence()
            .filter { sp -> sp.getUuid()!! == uuid }
            .firstOrNull()!!
    }
}
