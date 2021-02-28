package io.sparkled.model.animation

import io.sparkled.model.entity.v2.StagePropEntity

/**
 * Pairs up a sequence channel with a stage prop.
 */
data class ChannelPropPair(val channel: SequenceChannelEffects, val stageProp: StagePropEntity)
