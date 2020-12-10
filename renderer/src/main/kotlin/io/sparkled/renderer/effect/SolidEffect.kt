package io.sparkled.renderer.effect

import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SemVer
import io.sparkled.renderer.api.SparkledEffect
import io.sparkled.renderer.util.FillUtils

object SolidEffect : SparkledEffect<Unit> {

    override val id = "@sparkled/solid"
    override val version = SemVer(1, 0, 0)
    override val name = "Solid"

    override fun createState(ctx: RenderContext) {}

    override fun render(ctx: RenderContext, state: Unit) {
        for (i in 0 until ctx.ledCount) {
            FillUtils.fill(ctx, i, 1f)
        }
    }
}
