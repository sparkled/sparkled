package io.sparkled.renderer.effect.line

import io.sparkled.model.animation.param.Param
import io.sparkled.renderer.api.SemVer
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SparkledEffect
import io.sparkled.renderer.util.FillUtils
import io.sparkled.renderer.util.ParamUtils
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

object BuildLineEffect : SparkledEffect<Unit> {

    override val id = "@sparkled/build-line"
    override val name = "Build Line"
    override val version = SemVer(1, 0, 0)
    override val params = listOf(
        Param.int("SEGMENTS", "Segments", 4),
        Param.boolean("REVERSE", "Reverse Build Direction", false)
    )

    override fun createState(ctx: RenderContext) {}

    override fun render(ctx: RenderContext, state: Unit) {
        val segments = ParamUtils.getInt(ctx.effect, "SEGMENTS", 4)
        val reverse = ParamUtils.getBoolean(ctx.effect, "REVERSE", false)
        val ledCount = ctx.frame.ledCount
        val lineLength = ceil(ledCount / segments.toFloat()).toInt()

        for (i in 0 until segments) {
            var startPos = ledCount * (i + 1) + lineLength * i
            startPos -= round((ledCount * (segments)) * ctx.progress).toInt()
            startPos = min(ledCount, max(lineLength * i, startPos))

            var endPos = startPos + lineLength
            endPos = min(ledCount, max(lineLength * i, endPos))

            for (j in startPos until endPos) {
                val ledIndex = if (reverse) ledCount - j - 1 else j
                FillUtils.fill(ctx, ledIndex, 1f)
            }
        }
    }
}
