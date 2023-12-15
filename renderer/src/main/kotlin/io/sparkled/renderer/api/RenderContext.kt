package io.sparkled.renderer.api

import io.sparkled.model.StageModel
import io.sparkled.model.StagePropModel
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.render.RenderedFrame
import io.sparkled.model.render.RenderedStagePropData
import java.awt.image.BufferedImage

data class RenderContext(
    val stage: StageModel,
    val framesPerSecond: Int,
    val channel: RenderedStagePropData,
    val frame: RenderedFrame,
    val stageProp: StagePropModel,
    val effect: Effect,
    val progress: Float,
    private val fills: Map<String, SparkledFill>,
    val loadGif: (String) -> List<BufferedImage>,
) {
    val fill = fills[effect.fill.type]
    val pixelCount = stageProp.ledCount
}
