package io.sparkled.renderer.effect

import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.StatelessSparkledEffect
import io.sparkled.renderer.util.FillUtils

object SolidEffect : StatelessSparkledEffect {

    override val id = "sparkled:solid:1.0.0"
    override val name = "Solid"

    override fun render(ctx: RenderContext) {
        repeat(ctx.pixelCount) {
            FillUtils.fill(ctx, it, 1f)
        }
    }
}
