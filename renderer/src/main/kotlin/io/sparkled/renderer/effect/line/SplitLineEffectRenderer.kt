package io.sparkled.renderer.effect.line

import io.sparkled.model.animation.param.ParamCode
import io.sparkled.renderer.context.RenderContext
import io.sparkled.renderer.effect.EffectRenderer
import io.sparkled.renderer.effect.line.LineEffectUtils.renderLine
import io.sparkled.renderer.util.ParamUtils

class SplitLineEffectRenderer : EffectRenderer() {

    public override fun render(ctx: RenderContext) {
        val end = ctx.channel.ledCount
        val evenLedCount = end % 2 == 0
        val middle = end / 2
        val lineLength = ParamUtils.getDecimalValue(ctx.effect, ParamCode.LENGTH, 1f)

        // <<<<<----- (Line moves from middle to start).
        renderLine(ctx, if (evenLedCount) middle - 1 else middle, 0, lineLength)

        // ----->>>>> (Line moves from middle to end).
        renderLine(ctx, middle, end - 1, lineLength)
    }
}
