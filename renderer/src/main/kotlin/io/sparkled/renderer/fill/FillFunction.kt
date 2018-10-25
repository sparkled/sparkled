package io.sparkled.renderer.fill

import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.animation.fill.Fill
import io.sparkled.model.render.Led
import io.sparkled.renderer.context.RenderContext

interface FillFunction {

    /**
     * Fill the provided [Led] using the [Fill] configuration of the provided [Effect].
     */
    fun fill(ctx: RenderContext, led: Led, alpha: Float)
}
