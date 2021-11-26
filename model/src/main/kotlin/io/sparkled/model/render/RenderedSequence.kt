package io.sparkled.model.render

/**
 * Serializable representation of a rendered sequence, useful for persisting to disk.
 */
data class RenderedSequence(
    val sequenceId: Int,
    val startFrame: Int,
    val frameCount: Int,
    val stageProps: Map<String, RenderedSequenceStageProp>,
)
