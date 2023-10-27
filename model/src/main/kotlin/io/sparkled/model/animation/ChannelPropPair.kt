package io.sparkled.model.animation

import io.sparkled.model.StagePropModel

/**
 * Pairs up a sequence channel with a stage prop.
 */
data class ChannelPropPair(val channel: SequenceChannelEffects, val stageProp: StagePropModel)
