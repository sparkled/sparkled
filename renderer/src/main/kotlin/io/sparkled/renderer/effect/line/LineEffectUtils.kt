package io.sparkled.renderer.effect.line

import io.sparkled.model.util.MathUtils
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.util.FillUtils

object LineEffectUtils {

    /**
     * @param ctx The render context
     * @param startLed The first LED of the line (inclusive)
     * @param endLed The last LED of the line (inclusive)
     * @param lineLength The length of the line to render
     */
    fun renderLine(ctx: RenderContext, startLed: Int, endLed: Int, lineLength: Float) {
        val direction = if (startLed < endLed) 1 else -1
        val lineOffset = direction * lineLength

        val lineMin = startLed - lineOffset
        val lineMax = (endLed + direction).toFloat()

        val lineStart = lineMin + (lineMax - lineMin) * ctx.progress
        val lineEnd = lineStart + lineOffset

        var i = startLed
        while (i != endLed + direction) {
            val alpha = MathUtils.getOverlap(i.toFloat(), i.toFloat() + direction, lineStart, lineEnd)
            FillUtils.fill(ctx, i, alpha)
            i += direction
        }
    }
}
