package io.sparkled.renderer.effect.line

import io.sparkled.model.animation.param.Param
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SemVer
import io.sparkled.renderer.api.SparkledEffect
import io.sparkled.renderer.effect.line.LineEffectUtils.renderLine
import io.sparkled.renderer.util.ParamUtils

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
        val endLed = ctx.ledCount - 1
        val lineLength = ParamUtils.getFloat(ctx.effect, Params.LENGTH.name, 1f)

        renderLine(ctx, startLed, endLed, lineLength)
    }
}
