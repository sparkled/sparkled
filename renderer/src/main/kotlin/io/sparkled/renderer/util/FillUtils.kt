package io.sparkled.renderer.util

import io.sparkled.model.animation.fill.BlendMode
import io.sparkled.renderer.api.RenderContext
import org.slf4j.LoggerFactory
import java.awt.Color

object FillUtils {

    private val logger = LoggerFactory.getLogger(FillUtils::class.java)

    // TODO effectColor is an intermediate step. At present, we have the concept of fills and effects. These concepts
    //      will be merged into one, where effects can stack and act as a mask to combine effects.
    fun fill(ctx: RenderContext, ledIndex: Int, alpha: Float, effectColor: Color? = null) {
        val frame = ctx.frame

        if (alpha < 0 || alpha > 1) {
            val frameNumber = frame.frameNumber
            logger.warn("Alpha {} for led #{} in frame #{} is invalid, skipping.", alpha, ledIndex, frameNumber)
        } else if (ledIndex >= 0 && ledIndex < frame.ledCount) {
            val index = if (ctx.stageProp.reverse) frame.ledCount - ledIndex - 1 else ledIndex
            val led = frame.getLed(index)
            val fillColor = effectColor ?: ctx.fill?.getFill(ctx, ledIndex) ?: Color.BLACK

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
