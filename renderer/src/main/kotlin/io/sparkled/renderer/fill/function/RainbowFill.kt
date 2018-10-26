package io.sparkled.renderer.fill.function

import io.sparkled.model.animation.param.ParamName
import io.sparkled.model.render.Led
import io.sparkled.renderer.context.RenderContext
import io.sparkled.renderer.fill.FillFunction
import io.sparkled.renderer.util.ParamUtils
import java.awt.Color

class RainbowFill : FillFunction {

    override fun fill(ctx: RenderContext, led: Led, alpha: Float) {
        val cycleCount = ParamUtils.getDecimalValue(ctx.effect.getFill()!!, ParamName.CYCLE_COUNT)
        val cyclesPerSecond = ParamUtils.getDecimalValue(ctx.effect.getFill()!!, ParamName.CYCLES_PER_SECOND)

        val frame = ctx.frame
        val ledPosition = led.ledNumber.toFloat() / frame.ledCount * cycleCount
        val progress = frame.frameNumber.toFloat() / ctx.sequence.getFramesPerSecond()!! * cyclesPerSecond
        val hue = ledPosition + progress % 1f

        val color = Color.getHSBColor(hue, SATURATION, alpha)
        led.addColor(color)
    }

    companion object {
        private const val SATURATION = .95f
    }
}
