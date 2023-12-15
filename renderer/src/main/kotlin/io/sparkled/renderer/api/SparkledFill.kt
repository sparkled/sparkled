package io.sparkled.renderer.api

import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.animation.fill.Fill
import java.awt.Color

interface SparkledFill : SparkledPlugin {

    /**
     * @return the fill color for a given LED using the [Fill] configuration of the provided [Effect].
     */
    fun getFill(ctx: RenderContext, pixelIndex: Int): Color
}
