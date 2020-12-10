package io.sparkled.renderer.api

import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.StageProp
import io.sparkled.model.render.RenderedFrame
import io.sparkled.model.render.RenderedStagePropData

data class RenderContext(
    val sequence: Sequence,
    val channel: RenderedStagePropData,
    val frame: RenderedFrame,
    val stageProp: StageProp,
    val effect: Effect,
    val progress: Float,
    private val fills: Map<String, SparkledFill>,
) {
    val fill
        get() = fills[effect.fill.type]
    
    val ledCount = stageProp.getLedCount()!!
}
