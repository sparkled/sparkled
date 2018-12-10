package io.sparkled.model.render

/**
 * The result of a sequence render.
 */
data class RenderResult(val stageProps: RenderedStagePropDataMap, val frameCount: Int)
