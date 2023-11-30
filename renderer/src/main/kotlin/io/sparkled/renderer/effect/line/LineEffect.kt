package io.sparkled.renderer.effect.line

import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.StatelessSparkledEffect
import io.sparkled.renderer.effect.line.LineEffectUtils.renderLine
import io.sparkled.renderer.parameter.DecimalParameter

object LineEffect : StatelessSparkledEffect {

    override val id = "sparkled:line:1.0.0"
    override val name = "Line"

    private val lineLength by DecimalParameter(displayName = "Line length", defaultValue = 1f)

    override fun createState(ctx: RenderContext) {}

    override fun render(ctx: RenderContext) {

        val startLed = 0
        val endLed = ctx.pixelCount - 1
        val lineLength = lineLength.get(ctx)

        renderLine(ctx, startLed, endLed, lineLength)
    }
}
