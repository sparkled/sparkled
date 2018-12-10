package io.sparkled.renderer.util

import io.sparkled.renderer.context.RenderContext
import io.sparkled.renderer.fill.FillFunctions
import org.slf4j.LoggerFactory

object FillUtils {

    private val logger = LoggerFactory.getLogger(FillUtils::class.java)

    fun fill(ctx: RenderContext, ledIndex: Int, alpha: Float) {
        val frame = ctx.frame

        if (alpha < 0 || alpha > 1) {
            val frameNumber = frame.frameNumber
            logger.warn("Alpha {} for led #{} in frame #{} is invalid, skipping.", alpha, ledIndex, frameNumber)
        } else if (ledIndex >= 0 && ledIndex < frame.ledCount) {
            val index = if (ctx.stageProp.isReverse()!!) frame.ledCount - ledIndex - 1 else ledIndex
            val led = frame.getLed(index)
            FillFunctions[ctx.effect.fill.type].fill(ctx, led, ledIndex, alpha)
        }
    }
}
