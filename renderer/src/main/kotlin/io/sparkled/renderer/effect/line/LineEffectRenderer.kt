package io.sparkled.renderer.effect.line

import io.sparkled.model.animation.param.ParamCode
import io.sparkled.renderer.context.RenderContext
import io.sparkled.renderer.effect.EffectRenderer
import io.sparkled.renderer.effect.line.LineEffectUtils.renderLine
import io.sparkled.renderer.util.ParamUtils

class LineEffectRenderer : EffectRenderer() {

    public override fun render(ctx: RenderContext) {
        val startLed = 0
        val endLed = ctx.channel.ledCount - 1
        val lineLength = ParamUtils.getDecimalValue(ctx.effect, ParamCode.LENGTH, 1f)

        renderLine(ctx, startLed, endLed, lineLength)
    }
}
