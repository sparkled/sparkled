package io.sparkled.renderer.fill

import io.sparkled.model.animation.param.Param
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SemVer
import io.sparkled.renderer.api.SparkledFill
import java.awt.Color

/**
 * Fills all pixels with the same color.
 * Effect parameters:
 *  - COLOR: The color to fill.
 */
object SingleColorFill : SparkledFill {

    enum class Params { COLOR }

    override val id = "@sparkled/single-color"
    override val name = "Single Color"
    override val version = SemVer(1, 0, 0)
    override val params = listOf(
        Param.color(Params.COLOR.name, "Color", "#ff0000")
    )

    override fun getFill(ctx: RenderContext, pixelIndex: Int): Color {
        return ctx.getParam(ctx.effect.fill, Params.COLOR, Color::class, Color.MAGENTA)
    }
}
