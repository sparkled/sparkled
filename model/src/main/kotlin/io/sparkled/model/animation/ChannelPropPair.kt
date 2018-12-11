package io.sparkled.model.animation

import io.sparkled.model.entity.StageProp

/**
 * Pairs up a sequence channel with a stage prop.
 */
data class ChannelPropPair(val channel: SequenceChannelEffects, val stageProp: StageProp)
