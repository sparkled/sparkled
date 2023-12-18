package io.sparkled.renderer.effect.line

import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SparkledEffect
import io.sparkled.renderer.parameter.BooleanParameter
import io.sparkled.renderer.parameter.IntParameter
import io.sparkled.renderer.util.FillUtils
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

object BuildLineEffect : SparkledEffect<Unit> {

    override val id = "sparkled:build-line:1.0.0"
    override val name = "Build Line"

    private val segments by IntParameter(displayName = "Segments", defaultValue = 4)
    private val reverse by BooleanParameter(displayName = "Reverse build direction?", defaultValue = false)

    override fun createState(ctx: RenderContext) {}

    override fun render(ctx: RenderContext, state: Unit) {
        val segmentsValue = segments.get(ctx)
        val ledCount = ctx.pixelCount
        val lineLength = ceil(ledCount / segmentsValue.toFloat()).toInt()

        for (i in 0 until segmentsValue) {
            var startPos = ledCount * (i + 1) + lineLength * i
            startPos -= round((ledCount * segmentsValue) * ctx.progress).toInt()
            startPos = min(ledCount, max(lineLength * i, startPos))

            var endPos = startPos + lineLength
            endPos = min(ledCount, max(lineLength * i, endPos))

            for (j in startPos until endPos) {
                val pixelIndex = if (reverse.get(ctx)) ledCount - j - 1 else j
                FillUtils.fill(ctx, pixelIndex, 1f)
            }
        }
    }
}
