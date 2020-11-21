package io.sparkled.renderer.fill

import io.sparkled.model.animation.param.Param
import io.sparkled.model.util.MathUtils
import io.sparkled.renderer.api.SemVer
import io.sparkled.renderer.api.SparkledFill
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.util.ParamUtils
import java.awt.Color
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max

/**
 * Fills pixels with an N-color gradient, where N > 0. The colors are distributed equally.
 * Effect parameters:
 *  - COLORS: The colors that make up the gradient.
 *  - BLEND_HARDNESS: The amount of color mixing (0 is full mixing, 100 is no mixing, resulting in solid colors).
 */
object GradientFill : SparkledFill {

    override val id = "@sparkled/gradient"
    override val name = "Gradient"
    override val version = SemVer(1, 0, 0)
    override val params = listOf(
        Param.colors("COLORS", "Colors", listOf("#ff0000", "#0000ff")),
        Param.int("REPETITIONS", "Color Repetitions", 1),
        Param.decimal("BLEND_HARDNESS", "Blend Hardness", 0.0),
        Param.decimal("CYCLES_PER_SECOND", "Cycles Per Second", 0.0)
    )

    override fun getFill(ctx: RenderContext, ledIndex: Int): Color {
        val fill = ctx.effect.fill

        val colors = ParamUtils.getColors(fill, "COLORS", Color.MAGENTA)
        val repetitions = ParamUtils.getInt(fill, "COLOR_REPETITIONS", 1)
        val colorCount = colors.size * max(1, repetitions)

        val ledIndexNormalised = ledIndex / ctx.frame.ledCount.toFloat()

        val cyclesPerSecond = ParamUtils.getFloat(fill, "CYCLES_PER_SECOND", 0f)
        val cycleProgress = cyclesPerSecond * (ctx.frame.frameNumber.toFloat() / ctx.sequence.getFramesPerSecond()!!)
        val gradientProgress = ledIndexNormalised + cycleProgress

        val colorProgress = gradientProgress * (colorCount - 1)
        val color1 = colors[floor(colorProgress).toInt() % colors.size]
        val color2 = colors[ceil(colorProgress).toInt() % colors.size]

        val hardness = ParamUtils.getFloat(fill, "BLEND_HARDNESS", 0f) / 100f
        val blend = getBlend(colorProgress % 1f, hardness)
        return interpolate(color1, blend, color2)
    }

    /**
     * Depending on the hardness value, the blending needs to be adjusted. A hardness of 0 is a pure linear blend, and
     * increasing the hardness means reducing the blend area between adjacent colors until a hardness value of 100,
     * which has no color blending whatsoever.
     */
    private fun getBlend(gradientProgress: Float, hardness: Float): Float {
        val blend = gradientProgress - floor(gradientProgress)
        val hardnessThreshold = hardness / 2

        return when {
            hardnessThreshold == 0f -> blend
            blend < hardnessThreshold -> 0f
            blend < .5f -> MathUtils.map(blend, hardnessThreshold, .5f, 0f, .5f)
            blend >= 1f - hardnessThreshold -> 1f
            else -> MathUtils.map(blend, .5f, .5f + hardnessThreshold, .5f + hardnessThreshold, 1f)
        }
    }

    private fun interpolate(color1: Color, blend: Float, color2: Color): Color {
        val r = color1.red + (color2.red - color1.red) * blend
        val g = color1.green + (color2.green - color1.green) * blend
        val b = color1.blue + (color2.blue - color1.blue) * blend

        return Color(r / 255, g / 255, b / 255)
    }
}
