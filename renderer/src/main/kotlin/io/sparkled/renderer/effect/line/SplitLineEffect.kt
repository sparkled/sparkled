package io.sparkled.renderer.effect.line

import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SparkledEffect
import io.sparkled.renderer.effect.line.LineEffectUtils.renderLine
import io.sparkled.renderer.parameter.DecimalParameter

object SplitLineEffect : SparkledEffect<Unit> {

    override val id = "sparkled:split-line:1.0.0"
    override val name = "Split Line"

    private val lineLength by DecimalParameter(displayName = "Line length", defaultValue = 1f)

    override fun createState(ctx: RenderContext) {}

    override fun render(ctx: RenderContext, state: Unit) {
        val end = ctx.pixelCount
        val evenLedCount = end % 2 == 0
        val middle = end / 2
        val lineLength = lineLength.get(ctx)

        // <<<<<----- (Line moves from middle to start).
        renderLine(ctx, if (evenLedCount) middle - 1 else middle, 0, lineLength)

        // ----->>>>> (Line moves from middle to end).
        renderLine(ctx, middle, end - 1, lineLength)
    }
}
