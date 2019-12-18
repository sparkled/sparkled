package io.sparkled.renderer.fill

import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.animation.fill.Fill
import io.sparkled.renderer.context.RenderContext
import java.awt.Color

interface FillFunction {

    /**
     * @return the fill color for a given LED using the [Fill] configuration of the provided [Effect].
     */
    fun getFill(ctx: RenderContext, ledIndex: Int): Color
}
