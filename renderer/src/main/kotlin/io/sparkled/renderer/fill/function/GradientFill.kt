package io.sparkled.renderer.fill.function

import io.sparkled.model.animation.param.ParamCode
import io.sparkled.model.render.Led
import io.sparkled.model.util.MathUtils
import io.sparkled.renderer.context.RenderContext
import io.sparkled.renderer.fill.FillFunction
import io.sparkled.renderer.util.ColorUtils
import io.sparkled.renderer.util.ParamUtils
import java.awt.Color
import kotlin.math.ceil
import kotlin.math.floor

/**
 * Fills pixels with an N-color gradient, where N > 0. The colours are distributed equally.
 * Effect parameters:
 *  - COLORS: The colours that make up the gradient.
 *  - BLEND_HARDNESS: The amount of colour mixing (0 is full mixing, 100 is no mixing, resulting in solid colors).
 */
class GradientFill : FillFunction {

    override fun fill(ctx: RenderContext, led: Led, ledIndex: Int, alpha: Float) {
        val fill = ctx.effect.fill

        val colors = ParamUtils.getColorsValue(fill, ParamCode.COLORS, Color.MAGENTA)

        val ledIndexNormalised = ledIndex / ctx.frame.ledCount.toFloat()

        val cyclesPerSecond = ParamUtils.getDecimalValue(fill, ParamCode.CYCLES_PER_SECOND, 0f)
        val cycleProgress = cyclesPerSecond * (ctx.frame.frameNumber.toFloat() / ctx.sequence.getFramesPerSecond()!!)
        val gradientProgress = ledIndexNormalised + cycleProgress

        val colorProgress = gradientProgress * (colors.size - 1)
        val color1 = colors[floor(colorProgress).toInt() % colors.size]
        val color2 = colors[ceil(colorProgress).toInt() % colors.size]

        val hardness = ParamUtils.getDecimalValue(fill, ParamCode.BLEND_HARDNESS, 0f) / 100f
        val blend = getBlend(colorProgress % 1f, hardness)
        val interpolatedColor = interpolate(color1, blend, color2)

        val adjustedColor = ColorUtils.adjustBrightness(interpolatedColor, alpha)
        led.addColor(adjustedColor)
    }

    /**
     * Depending on the hardness value, the blending needs to be adjusted. A hardness of 0 is a pure linear blend, and
     * increasing the hardness means reducing the blend area between adjacent colours until a hardness value of 100,
     * which has no colour blending whatsoever.
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
