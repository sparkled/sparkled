package io.sparkled.renderer.effect.line

import io.sparkled.model.animation.param.Param
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SemVer
import io.sparkled.renderer.api.SparkledEffect
import io.sparkled.renderer.effect.line.LineEffectUtils.renderLine

object LineEffect : SparkledEffect<Unit> {

    enum class Params { LENGTH }

    override val id = "@sparkled/line"
    override val name = "Line"
    override val version = SemVer(1, 0, 0)
    override val params = listOf(
        Param.int(Params.LENGTH.name, "Length", 1)
    )

    override fun createState(ctx: RenderContext) {}

    override fun render(ctx: RenderContext, state: Unit) {
        val startLed = 0
        val endLed = ctx.pixelCount - 1
        val lineLength = ctx.getParam(ctx.effect, Params.LENGTH, Float::class, 1f)

        renderLine(ctx, startLed, endLed, lineLength)
    }
}
