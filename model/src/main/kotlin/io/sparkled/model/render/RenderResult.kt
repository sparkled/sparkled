package io.sparkled.model.render

import io.sparkled.model.annotation.GenerateClientType

/**
 * The result of a sequence render.
 */
@GenerateClientType
data class RenderResult(
    val stageProps: RenderedStagePropDataMap,
    val startFrame: Int,
    val frameCount: Int,
)
