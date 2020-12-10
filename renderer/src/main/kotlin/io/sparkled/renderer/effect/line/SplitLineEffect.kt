package io.sparkled.renderer.effect.line

import io.sparkled.model.animation.param.Param
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SemVer
import io.sparkled.renderer.api.SparkledEffect
import io.sparkled.renderer.effect.line.LineEffectUtils.renderLine
import io.sparkled.renderer.util.ParamUtils

object SplitLineEffect : SparkledEffect<Unit> {
    
    enum class Params { LENGTH }

    override val id = "@sparkled/split-line"
    override val name = "Split Line"
    override val version = SemVer(1, 0, 0)
    override val params = listOf(
        Param.int(Params.LENGTH.name, "Length", 1)
    )

    override fun createState(ctx: RenderContext) {}

    override fun render(ctx: RenderContext, state: Unit) {
        val end = ctx.ledCount
        val evenLedCount = end % 2 == 0
        val middle = end / 2
        val lineLength = ParamUtils.getFloat(ctx.effect, Params.LENGTH.name, 1f)

        // <<<<<----- (Line moves from middle to start).
        renderLine(ctx, if (evenLedCount) middle - 1 else middle, 0, lineLength)

        // ----->>>>> (Line moves from middle to end).
        renderLine(ctx, middle, end - 1, lineLength)
    }
}
