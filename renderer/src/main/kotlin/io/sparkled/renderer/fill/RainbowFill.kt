package io.sparkled.renderer.fill

import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SparkledFill
import io.sparkled.renderer.parameter.DecimalParameter
import java.awt.Color

/**
 * Fills pixels with a rainbow-ish effect based on a full rotation of the HSV hue circle.
 */
object RainbowFill : SparkledFill {

    override val id = "sparkled:rainbow:1.0.0"
    override val name = "Rainbow"

    /** The number of hue circle rotations to fit between the first and last pixel. */
    private val brightness by DecimalParameter(displayName = "Brightness (%)", defaultValue = 1f)

    /** The speed at which the hue circle rotates. */
    private val cycleCount by DecimalParameter(displayName = "Cycle count", defaultValue = 1f)

    /** The brightness of the rainbow. */
    private val cyclesPerSecond by DecimalParameter(displayName = "Cycles per second", defaultValue = 0.5f)

    override fun getFill(ctx: RenderContext, pixelIndex: Int): Color {
        val cycleCount = cycleCount.get(ctx)
        val cyclesPerSecond = cyclesPerSecond.get(ctx)
        val brightness = RainbowFill.brightness.get(ctx)

        val frame = ctx.frame
        val ledPosition = pixelIndex.toFloat() / frame.ledCount * cycleCount
        val progress = (frame.frameIndex - ctx.effect.startFrame).toFloat() / ctx.framesPerSecond * cyclesPerSecond
        val hue = ledPosition + progress % 1f

        return Color.getHSBColor(hue, SATURATION, brightness)
    }

    private const val SATURATION = .95f
}
