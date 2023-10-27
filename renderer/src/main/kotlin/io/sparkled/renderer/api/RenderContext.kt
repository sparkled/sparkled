package io.sparkled.renderer.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.sparkled.model.SequenceModel
import io.sparkled.model.StageModel
import io.sparkled.model.StagePropModel
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.render.RenderedFrame
import io.sparkled.model.render.RenderedStagePropData
import java.awt.image.BufferedImage

data class RenderContext(
    val stage: StageModel,
    val sequence: SequenceModel,
    val channel: RenderedStagePropData,
    val frame: RenderedFrame,
    val stageProp: StagePropModel,
    val effect: Effect,
    val progress: Float,
    private val fills: Map<String, SparkledFill>,
    val loadGif: (String) -> List<BufferedImage>,
) {
    val fill
        get() = fills[effect.fill.type]

    val ledCount = stageProp.ledCount

    val objectMapper = sharedObjectMapper

    companion object {
        private val sharedObjectMapper = jacksonObjectMapper()
    }
}
