package io.sparkled.renderer.effect.line

import io.sparkled.model.animation.param.ParamCode
import io.sparkled.renderer.context.RenderContext
import io.sparkled.renderer.effect.EffectRenderer
import io.sparkled.renderer.util.FillUtils
import io.sparkled.renderer.util.ParamUtils
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

class BuildLineEffectRenderer : EffectRenderer() {

    public override fun render(ctx: RenderContext) {
        val segments = ParamUtils.getIntegerValue(ctx.effect, ParamCode.SEGMENTS, 4)
        val lineLength = ceil(ctx.frame.ledCount / segments.toFloat()).toInt()

        for (i in 0 until segments) {
            var startPos = ctx.frame.ledCount * (i + 1) + lineLength * i
            startPos -= round((ctx.frame.ledCount * (segments)) * ctx.progress).toInt()
            startPos = min(ctx.frame.ledCount, max(lineLength * i, startPos))

            var endPos = startPos + lineLength
            endPos = min(ctx.frame.ledCount, max(lineLength * i, endPos))

            for (j in startPos until endPos) {
                FillUtils.fill(ctx, j, 1f)
            }
        }
    }
}
