package io.sparkled.renderer.fill.function

import io.sparkled.model.animation.param.ParamName
import io.sparkled.model.render.Led
import io.sparkled.renderer.context.RenderContext
import io.sparkled.renderer.fill.FillFunction
import io.sparkled.renderer.util.ParamUtils
import java.awt.Color

class RainbowFill : FillFunction {

    override fun fill(ctx: RenderContext, led: Led, alpha: Float) {
        val fill = ctx.effect.getFill()!!
        val cycleCount = ParamUtils.getDecimalValue(fill, ParamName.CYCLE_COUNT, 1f)
        val cyclesPerSecond = ParamUtils.getDecimalValue(fill, ParamName.CYCLES_PER_SECOND, 1f)
        val brightness = ParamUtils.getDecimalValue(fill, ParamName.BRIGHTNESS, 100f) / 100f

        val frame = ctx.frame
        val ledPosition = led.ledNumber.toFloat() / frame.ledCount * cycleCount
        val progress = frame.frameNumber.toFloat() / ctx.sequence.getFramesPerSecond()!! * cyclesPerSecond
        val hue = ledPosition + progress % 1f

        val color = Color.getHSBColor(hue, SATURATION, alpha * brightness)
        led.addColor(color)
    }

    companion object {
        private const val SATURATION = .95f
    }
}
