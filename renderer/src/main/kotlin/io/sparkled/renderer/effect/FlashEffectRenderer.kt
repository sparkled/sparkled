package io.sparkled.renderer.effect

import io.sparkled.renderer.context.RenderContext
import io.sparkled.renderer.util.FillUtils

class FlashEffectRenderer : EffectRenderer() {

    override fun render(ctx: RenderContext) {
        val alpha = getAlpha(ctx.progress)

        for (i in 0 until ctx.channel.ledCount) {
            FillUtils.fill(ctx, i, alpha)
        }
    }

    /**
     * @param progress The normalised progress.
     *
     * @return The progress, transformed into 0 > 1 > 0.
     */
    private fun getAlpha(progress: Float): Float {
        val alpha = 2 * if (progress < .5) progress else 1 - progress

        // Remove floating point inaccuracies to ensure a symmetrical flash.
        return Math.round(alpha * 10000f) / 10000f
    }
}
