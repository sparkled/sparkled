package io.sparkled.renderer.fill

import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SparkledFill
import io.sparkled.renderer.parameter.ColorParameter
import java.awt.Color

/**
 * Fills all pixels with the same color.
 */
object SingleColorFill : SparkledFill {

    override val id = "sparkled:single-color:1.0.0"
    override val name = "Single Color"

    /** The color to fill. */
    private val color by ColorParameter(displayName = "Color", defaultValue = Color.MAGENTA)

    override fun getFill(ctx: RenderContext, pixelIndex: Int): Color {
        return color.get(ctx)
    }
}
