package io.sparkled.renderer.util

import io.sparkled.model.render.Led
import io.sparkled.renderer.context.RenderContext
import io.sparkled.renderer.fill.FillFunctions

object FillUtils {

    fun fill(ctx: RenderContext, led: Led, alpha: Float) {
        FillFunctions.get(ctx.getEffect().getFill().getType()).fill(ctx, led, alpha)
    }
}
