package io.sparkled.renderer.util

import io.sparkled.model.animation.ChannelPropPair
import io.sparkled.model.animation.SequenceChannelEffects
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.model.entity.StageProp
import io.sparkled.model.util.GsonProvider
import java.util.UUID

import java.util.stream.Collectors.toList

/**
 * Pairs up sequence channels with their associated stage prop.
 */
object ChannelPropPairUtils {

    fun makePairs(sequenceChannels: List<SequenceChannel>, stageProps: List<StageProp>): List<ChannelPropPair> {
        return sequenceChannels.stream()
                .map({ sc -> getPair(sc, stageProps) })
                .collect(toList())
    }

    private fun getPair(sequenceChannel: SequenceChannel, stageProps: List<StageProp>): ChannelPropPair {
        return ChannelPropPair(convertChannelData(sequenceChannel), findStagePropByUuid(stageProps, sequenceChannel.getStagePropUuid()))
    }

    private fun convertChannelData(sequenceChannel: SequenceChannel): SequenceChannelEffects {
        return GsonProvider.get().fromJson(sequenceChannel.getChannelJson(), SequenceChannelEffects::class.java)
    }

    private fun findStagePropByUuid(stageProps: List<StageProp>, uuid: UUID): StageProp {
        return stageProps.stream()
                .filter({ sp -> sp.getUuid().equals(uuid) })
                .findFirst()
                .orElse(null)
    }
}
