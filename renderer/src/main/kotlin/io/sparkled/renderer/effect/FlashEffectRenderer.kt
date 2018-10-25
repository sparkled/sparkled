package io.sparkled.renderer.effect

import io.sparkled.renderer.context.RenderContext
import io.sparkled.renderer.util.FillUtils

class FlashEffectRenderer : EffectRenderer() {

    @Override
    fun render(ctx: RenderContext) {
        val alpha = getAlpha(ctx.getProgress())

        for (i in 0..ctx.getChannel().getLedCount() - 1) {
            FillUtils.fill(ctx, ctx.getFrame().getLed(i), alpha)
        }
    }

    /**
     * @param progress The 0 > 1 progress
     * *
     * @return The progress, transformed into 0 > 1 > 0
     */
    private fun getAlpha(progress: Float): Float {
        var alpha = progress * 2
        if (progress >= .5) {
            alpha = 1 - (progress - .5f) * 2
        }

        // Remove floating point inaccuracies to ensure a symmetrical flash.
        return Math.round(alpha * 10000f) / 10000f
    }
}
