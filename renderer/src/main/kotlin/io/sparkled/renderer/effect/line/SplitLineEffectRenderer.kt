package io.sparkled.renderer.effect.line

import io.sparkled.renderer.context.RenderContext

class SplitLineEffectRenderer : AbstractLineEffectRenderer() {

    public override fun render(ctx: RenderContext) {
        val end = ctx.channel.ledCount
        val evenLedCount = end % 2 == 0
        val middle = end / 2

        // <<<<<----- (Line moves from middle to start).
        renderLine(ctx, if (evenLedCount) middle - 1 else middle, 0)

        // ----->>>>> (Line moves from middle to end).
        renderLine(ctx, middle, end - 1)
    }
}