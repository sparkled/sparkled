package io.sparkled.renderer.effect.line

import io.sparkled.model.animation.param.Param
import io.sparkled.renderer.api.SemVer
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SparkledEffect
import io.sparkled.renderer.util.FillUtils
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

object BuildLineEffect : SparkledEffect<Unit> {

    enum class Params { SEGMENTS, REVERSE }

    override val id = "@sparkled/build-line"
    override val name = "Build Line"
    override val version = SemVer(1, 0, 0)
    override val params = listOf(
        Param.int(Params.SEGMENTS.name, "Segments", 4),
        Param.boolean(Params.REVERSE.name, "Reverse Build Direction", false)
    )

    override fun createState(ctx: RenderContext) {}

    override fun render(ctx: RenderContext, state: Unit) {
        val segments = ctx.getParam(ctx.effect, Params.SEGMENTS, Int::class, 4)
        val reverse = ctx.getParam(ctx.effect, Params.REVERSE, Boolean::class, false)
        val ledCount = ctx.pixelCount
        val lineLength = ceil(ledCount / segments.toFloat()).toInt()

        for (i in 0 until segments) {
            var startPos = ledCount * (i + 1) + lineLength * i
            startPos -= round((ledCount * (segments)) * ctx.progress).toInt()
            startPos = min(ledCount, max(lineLength * i, startPos))

            var endPos = startPos + lineLength
            endPos = min(ledCount, max(lineLength * i, endPos))

            for (j in startPos until endPos) {
                val pixelIndex = if (reverse) ledCount - j - 1 else j
                FillUtils.fill(ctx, pixelIndex, 1f)
            }
        }
    }
}
