package io.sparkled.model.render

import io.sparkled.model.UniqueId

/**
 * Serializable representation of a rendered sequence, useful for persisting to disk.
 */
data class RenderedSequence(
    val sequenceId: UniqueId,
    val startFrame: Int,
    val frameCount: Int,
    val stageProps: Map<String, RenderedSequenceStageProp>,
)
