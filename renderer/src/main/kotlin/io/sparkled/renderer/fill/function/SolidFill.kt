package io.sparkled.renderer.fill.function

import io.sparkled.model.animation.param.ParamCode
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

    override fun getFill(ctx: RenderContext, ledIndex: Int): Color {
        return ParamUtils.getColorValue(ctx.effect.fill, ParamCode.COLOR, Color.MAGENTA)
    }
}
