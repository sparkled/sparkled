package io.sparkled.renderer.fill

import io.sparkled.model.animation.param.Param
import io.sparkled.renderer.api.SemVer
import io.sparkled.renderer.api.SparkledFill
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.util.ParamUtils
import java.awt.Color

/**
 * Fills all pixels with the same color.
 * Effect parameters:
 *  - COLOR: The color to fill.
 */
object SingleColorFill : SparkledFill {

    override val id = "@sparkled/single_color"
    override val name = "Single Color"
    override val version = SemVer(1, 0, 0)
    override val params = listOf(
        Param.color("COLOR", "Color", "#ff0000")
    )

    override fun getFill(ctx: RenderContext, ledIndex: Int): Color {
        return ParamUtils.getColorValue(ctx.effect.fill, "COLOR", Color.MAGENTA)
    }
}
