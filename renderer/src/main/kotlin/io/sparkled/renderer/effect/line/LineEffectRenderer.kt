package io.sparkled.renderer.effect.line

import io.sparkled.renderer.context.RenderContext

class LineEffectRenderer : AbstractLineEffectRenderer() {

    public override fun render(ctx: RenderContext) {
        val startLed = 0
        val endLed = ctx.channel.ledCount - 1
        renderLine(ctx, startLed, endLed)
    }
}
