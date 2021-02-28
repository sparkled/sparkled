package io.sparkled.renderer.api

import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.entity.v2.SequenceEntity
import io.sparkled.model.entity.v2.StagePropEntity
import io.sparkled.model.render.RenderedFrame
import io.sparkled.model.render.RenderedStagePropData

data class RenderContext(
    val sequence: SequenceEntity,
    val channel: RenderedStagePropData,
    val frame: RenderedFrame,
    val stageProp: StagePropEntity,
    val effect: Effect,
    val progress: Float,
    private val fills: Map<String, SparkledFill>,
) {
    val fill
        get() = fills[effect.fill.type]

    val ledCount = stageProp.ledCount
}
