package io.sparkled.renderer.util

import io.sparkled.common.logging.getLogger
import io.sparkled.model.animation.fill.BlendMode
import io.sparkled.renderer.api.RenderContext
import java.awt.Color

object FillUtils {

    private val logger = getLogger<FillUtils>()

    // TODO effectColor is an intermediate step. At present, we have the concept of fills and effects. These concepts
    //      will be merged into one, where effects can stack and act as a mask to combine effects.
    /**
     * @param ignoreReverse used for location-aware effects, which only care about the position of the LED in 2D space.
     */
    fun fill(
        ctx: RenderContext,
        pixelIndex: Int,
        alpha: Float,
        effectColor: Color? = null,
        ignoreReverse: Boolean = false,
    ) {
        val targetPixels = ctx.effect.targetPixels
        if (targetPixels != null && !targetPixels[pixelIndex]) {
            return
        }

        val frame = ctx.frame
        val ledRange = ctx.channel.stagePropRanges[ctx.stageProp.id] ?: error("Stage prop range not found")

        if (alpha < 0 || alpha > 1) {
            val frameIndex = frame.frameIndex
            logger.warn(
                "Alpha is invalid, skipping.",
                "value" to alpha,
                "pixelIndex" to pixelIndex,
                "frameNumber" to frameIndex,
            )
        } else if (pixelIndex >= 0 && pixelIndex < ctx.stageProp.ledCount) {
            val reverse = !ignoreReverse && ctx.stageProp.reverse
            val index = if (reverse) ledRange.last - pixelIndex else ledRange.first + pixelIndex
            val led = frame.getLed(index)
            val fillColor = effectColor ?: ctx.fill?.getFill(ctx, pixelIndex) ?: Color.BLACK

            when (ctx.effect.fill.blendMode) {
                BlendMode.NORMAL -> led.setColor(fillColor, alpha)
                BlendMode.ADD -> {
                    val color = ColorUtils.adjustBrightness(fillColor, alpha)
                    led.addColor(color)
                }

                BlendMode.SUBTRACT -> {
                    val color = ColorUtils.adjustBrightness(fillColor, alpha)
                    led.subtractColor(color)
                }

                BlendMode.ALPHA_MASK -> {
                    val color = ColorUtils.adjustBrightness(fillColor, alpha)
                    led.maskColor(color)
                }
            }
        }
    }
}
