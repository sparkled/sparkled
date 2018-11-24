package io.sparkled.renderer.effect.line

import io.sparkled.model.animation.param.ParamName
import io.sparkled.renderer.context.RenderContext
import io.sparkled.renderer.effect.EffectRenderer
import io.sparkled.renderer.util.FillUtils
import io.sparkled.renderer.util.MathUtils
import io.sparkled.renderer.util.ParamUtils

abstract class AbstractLineEffectRenderer : EffectRenderer() {

    /**
     * @param ctx The render context
     * @param startLed The first LED of the line (inclusive)
     * @param endLed The last LED of the line (inclusive)
     */
    internal fun renderLine(ctx: RenderContext, startLed: Int, endLed: Int) {
        val direction = if (startLed < endLed) 1 else -1
        val lineOffset = direction * ParamUtils.getDecimalValue(ctx.effect, ParamName.LENGTH, 1f)

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
