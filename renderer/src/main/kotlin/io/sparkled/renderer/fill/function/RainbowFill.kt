package io.sparkled.renderer.fill.function

import io.sparkled.model.animation.param.ParamName
import io.sparkled.model.render.Led
import io.sparkled.renderer.context.RenderContext
import io.sparkled.renderer.fill.FillFunction
import io.sparkled.renderer.util.ParamUtils
import java.awt.Color

/**
 * Fills pixels with a rainbow-ish effect based on a full rotation of the HSV hue circle.
 * Effect parameters:
 *  - CYCLE_COUNT: The number of hue circle rotations to fit between the first and last pixel.
 *  - CYCLES_PER_SECOND: The speed at which the hue circle rotates.
 *  - BRIGHTNESS: The brightness of the rainbow.
 */
class RainbowFill : FillFunction {

    override fun fill(ctx: RenderContext, led: Led, ledIndex: Int, alpha: Float) {
        val fill = ctx.effect.fill
        val cycleCount = ParamUtils.getDecimalValue(fill, ParamName.CYCLE_COUNT, 1f)
        val cyclesPerSecond = ParamUtils.getDecimalValue(fill, ParamName.CYCLES_PER_SECOND, 1f)
        val brightness = ParamUtils.getDecimalValue(fill, ParamName.BRIGHTNESS, 100f) / 100f

        val frame = ctx.frame
        val ledPosition = ledIndex.toFloat() / frame.ledCount * cycleCount
        val progress = frame.frameNumber.toFloat() / ctx.sequence.getFramesPerSecond()!! * cyclesPerSecond
        val hue = ledPosition + progress % 1f

        val color = Color.getHSBColor(hue, SATURATION, alpha * brightness)
        led.addColor(color)
    }

    companion object {
        private const val SATURATION = .95f
    }
}
