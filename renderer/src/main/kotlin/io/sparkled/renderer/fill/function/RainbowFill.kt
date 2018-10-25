package io.sparkled.renderer.fill.function

import io.sparkled.model.animation.param.ParamName
import io.sparkled.model.render.Led
import io.sparkled.model.render.RenderedFrame
import io.sparkled.renderer.context.RenderContext
import io.sparkled.renderer.fill.FillFunction
import io.sparkled.renderer.util.ParamUtils

import java.awt.*

class RainbowFill : FillFunction {

    @Override
    fun fill(ctx: RenderContext, led: Led, alpha: Float) {
        val cycleCount = ParamUtils.getDecimalValue(ctx.getEffect().getFill(), ParamName.CYCLE_COUNT)
        val cyclesPerSecond = ParamUtils.getDecimalValue(ctx.getEffect().getFill(), ParamName.CYCLES_PER_SECOND)

        val frame = ctx.getFrame()
        val ledPosition = led.getLedNumber() as Float / frame.getLedCount() * cycleCount
        val progress = frame.getFrameNumber() as Float / ctx.getSequence().getFramesPerSecond() * cyclesPerSecond
        val hue = ledPosition + progress % 1f

        val color = Color.getHSBColor(hue, SATURATION, alpha)
        led.addColor(color)
    }

    companion object {

        private val SATURATION = .95f
    }
}
