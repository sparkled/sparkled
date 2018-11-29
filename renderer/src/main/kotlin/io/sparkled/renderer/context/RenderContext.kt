package io.sparkled.renderer.context

import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.entity.Sequence
import io.sparkled.model.render.RenderedFrame
import io.sparkled.model.render.RenderedStagePropData

data class RenderContext(
    val sequence: Sequence,
    val channel: RenderedStagePropData,
    val frame: RenderedFrame,
    val effect: Effect,
    val progress: Float
)
