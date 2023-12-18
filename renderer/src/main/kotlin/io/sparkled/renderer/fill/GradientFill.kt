package io.sparkled.renderer.fill

import io.sparkled.model.animation.Colors
import io.sparkled.model.util.MathUtils
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SparkledFill
import io.sparkled.renderer.parameter.ColorsParameter
import io.sparkled.renderer.parameter.DecimalParameter
import io.sparkled.renderer.parameter.IntParameter
import java.awt.Color
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max

/**
 * Fills pixels with an N-color gradient, where N > 0. The colors are distributed equally.
 */
object GradientFill : SparkledFill {

    override val id = "sparkled:gradient:1.0.0"
    override val name = "Gradient"

    /** The colors that make up the gradient. */
    private val colors by ColorsParameter(displayName = "Colors", defaultValue = Colors(Color.MAGENTA))
    private val repetitions by IntParameter(displayName = "Color repetitions", defaultValue = 1)

    /** The amount of color mixing (0 is full mixing, 100 is no mixing, resulting in solid colors). */
    private val blendHardness by DecimalParameter(displayName = "Blend hardness", defaultValue = 0f)
    private val cyclesPerSecond by DecimalParameter(displayName = "Cycles per second", defaultValue = 0f)

    override fun getFill(ctx: RenderContext, pixelIndex: Int): Color {
        val colors = this.colors.get(ctx)
        val repetitions = repetitions.get(ctx)
        val colorCount = colors.size * max(1, repetitions)

        val pixelIndexNormalised = pixelIndex / ctx.pixelCount.toFloat()

        val cyclesPerSecond = cyclesPerSecond.get(ctx)
        val cycleProgress = cyclesPerSecond * (ctx.frame.frameIndex.toFloat() / ctx.framesPerSecond)
        val gradientProgress = (pixelIndexNormalised + cycleProgress)

        val colorProgress = gradientProgress * (colorCount - 1)
        val color1 = colors[floor(colorProgress).toInt() % colors.size]
        val color2 = colors[ceil(colorProgress).toInt() % colors.size]

        val hardness = blendHardness.get(ctx)
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
