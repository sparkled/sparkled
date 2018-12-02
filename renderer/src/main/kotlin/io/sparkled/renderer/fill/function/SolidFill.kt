package io.sparkled.renderer.fill.function

import io.sparkled.model.animation.param.ParamName
import io.sparkled.model.render.Led
import io.sparkled.renderer.context.RenderContext
import io.sparkled.renderer.fill.FillFunction
import io.sparkled.renderer.util.ColorUtils
import io.sparkled.renderer.util.ParamUtils
import java.awt.Color

/**
 * Fills all pixels with the same colour.
 * Effect parameters: none.
 */
class SolidFill : FillFunction {

    override fun fill(ctx: RenderContext, led: Led, alpha: Float) {
        val color = ParamUtils.getColorValue(ctx.effect.getFill()!!, ParamName.COLOR, Color.MAGENTA)
        val adjustedColor = ColorUtils.adjustBrightness(color, alpha)
        led.addColor(adjustedColor)
    }
}
