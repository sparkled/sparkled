package io.sparkled.renderer.fill

import io.sparkled.model.animation.param.Param
import io.sparkled.renderer.api.SemVer
import io.sparkled.renderer.api.SparkledFill
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.util.ParamUtils
import java.awt.Color

/**
 * Fills pixels with a rainbow-ish effect based on a full rotation of the HSV hue circle.
 * Effect parameters:
 *  - CYCLE_COUNT: The number of hue circle rotations to fit between the first and last pixel.
 *  - CYCLES_PER_SECOND: The speed at which the hue circle rotates.
 *  - BRIGHTNESS: The brightness of the rainbow.
 */
object RainbowFill : SparkledFill {

    override val id = "@sparkled/rainbow"
    override val name = "Rainbow"
    override val version = SemVer(1, 0, 0)
    override val params = listOf(
        Param.decimal("BRIGHTNESS", "Brightness (%)", 100.0),
        Param.decimal("CYCLE_COUNT", "Cycle Count", 1.0),
        Param.decimal("CYCLES_PER_SECOND", "Cycles Per Second", 0.5)
    )

    override fun getFill(ctx: RenderContext, ledIndex: Int): Color {
        val fill = ctx.effect.fill
        val cycleCount = ParamUtils.getFloat(fill, "CYCLE_COUNT", 1f)
        val cyclesPerSecond = ParamUtils.getFloat(fill, "CYCLES_PER_SECOND", 1f)
        val brightness = ParamUtils.getFloat(fill, "BRIGHTNESS", 100f) / 100f

        val frame = ctx.frame
        val ledPosition = ledIndex.toFloat() / frame.ledCount * cycleCount
        val progress = frame.frameNumber.toFloat() / ctx.sequence.getFramesPerSecond()!! * cyclesPerSecond
        val hue = ledPosition + progress % 1f

        return Color.getHSBColor(hue, SATURATION, brightness)
    }
    
    private const val SATURATION = .95f
}
