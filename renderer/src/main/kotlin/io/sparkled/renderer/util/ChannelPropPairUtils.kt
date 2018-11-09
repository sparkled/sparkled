package io.sparkled.renderer.util

import io.sparkled.model.animation.ChannelPropPair
import io.sparkled.model.animation.SequenceChannelEffects
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.model.entity.StageProp
import io.sparkled.model.util.GsonProvider
import java.util.UUID

/**
 * Pairs up sequence channels with their associated stage prop.
 */
object ChannelPropPairUtils {

    fun makePairs(sequenceChannels: List<SequenceChannel>, stageProps: List<StageProp>): List<ChannelPropPair> {
        return sequenceChannels.asSequence().map { sc -> getPair(sc, stageProps) }.toList()
    }

    private fun getPair(sequenceChannel: SequenceChannel, stageProps: List<StageProp>): ChannelPropPair {
        return ChannelPropPair(
            convertChannelData(sequenceChannel),
            findStagePropByUuid(stageProps, sequenceChannel.getStagePropUuid()!!)
        )
    }

    private fun convertChannelData(sequenceChannel: SequenceChannel): SequenceChannelEffects {
        return GsonProvider.get().fromJson(sequenceChannel.getChannelJson(), SequenceChannelEffects::class.java)
    }

    private fun findStagePropByUuid(stageProps: List<StageProp>, uuid: UUID): StageProp {
        return stageProps
            .asSequence()
            .filter { sp -> sp.getUuid()!! == uuid }
            .firstOrNull()!!
    }
}
