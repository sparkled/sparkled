package io.sparkled.renderer.api

import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.entity.v2.SequenceEntity
import io.sparkled.model.entity.v2.StageEntity
import io.sparkled.model.entity.v2.StagePropEntity
import io.sparkled.model.render.RenderedFrame
import io.sparkled.model.render.RenderedStagePropData
import java.awt.image.BufferedImage

data class RenderContext(
    val stage: StageEntity,
    val sequence: SequenceEntity,
    val channel: RenderedStagePropData,
    val frame: RenderedFrame,
    val stageProp: StagePropEntity,
    val effect: Effect,
    val progress: Float,
    private val fills: Map<String, SparkledFill>,
    val loadGif: (String) -> List<BufferedImage>,
) {
    val fill
        get() = fills[effect.fill.type]

    val ledCount = stageProp.ledCount
}
