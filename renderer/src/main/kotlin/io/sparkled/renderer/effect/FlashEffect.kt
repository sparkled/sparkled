package io.sparkled.renderer.effect

import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.StatelessSparkledEffect
import io.sparkled.renderer.util.FillUtils
import kotlin.math.roundToInt

object FlashEffect : StatelessSparkledEffect {

    override val id = "sparkled:flash:1.0.0"
    override val name = "Flash"

    override fun render(ctx: RenderContext) {
        val alpha = getAlpha(ctx.progress)

        for (i in 0 until ctx.pixelCount) {
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
        return (alpha * 10000f).roundToInt() / 10000f
    }
}
