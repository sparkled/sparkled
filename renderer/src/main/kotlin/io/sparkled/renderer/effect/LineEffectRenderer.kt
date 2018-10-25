package io.sparkled.renderer.effect

import io.sparkled.model.animation.param.ParamName
import io.sparkled.renderer.context.RenderContext
import io.sparkled.renderer.util.FillUtils
import io.sparkled.renderer.util.ParamUtils

class LineEffectRenderer : EffectRenderer() {

    override fun render(ctx: RenderContext) {
        val startLed = 0
        val endLed = ctx.channel.ledCount - 1
        renderLine(ctx, startLed, endLed, false)
    }

    internal fun renderLine(ctx: RenderContext, startLed: Int, endLed: Int, mirror: Boolean) {
        val ledCount = endLed - startLed + 1
        val length = ParamUtils.getIntegerValue(ctx.effect, ParamName.LENGTH)

        val progress = if (mirror) 1 - ctx.progress else ctx.progress
        val firstLitLed = startLed + Math.round((ledCount + length) * progress) - length
        val lastLitLed = firstLitLed + length - 1

        if (firstLitLed <= endLed && lastLitLed >= 0) {
            for (i in constrain(startLed, endLed, firstLitLed)..constrain(startLed, endLed, lastLitLed)) {
                FillUtils.fill(ctx, ctx.frame.getLed(i), 1f)

                if (mirror) {
                    FillUtils.fill(ctx, ctx.frame.getLed(endLed * 2 + (1 - endLed % 2) - i), 1f)
                }
            }
        }
    }

    private fun constrain(min: Int, max: Int, value: Int): Int {
        return Math.max(min, Math.min(max, value))
    }
}
