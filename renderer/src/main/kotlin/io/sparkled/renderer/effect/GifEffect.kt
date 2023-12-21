package io.sparkled.renderer.effect

import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.StatelessSparkledEffect
import io.sparkled.renderer.parameter.StringParameter
import io.sparkled.renderer.util.FillUtils
import java.awt.Color
import kotlin.math.roundToInt

object GifEffect : StatelessSparkledEffect {

    override val id = "sparkled:gif:1.0.0"
    override val name = "Gif"

    private val filename by StringParameter(displayName = "Filename", defaultValue = "")

    override fun render(ctx: RenderContext) {
        val gifFrames = ctx.loadGif(filename.get(ctx))
        val points = ctx.stageProp.ledPositions.points

        val gifFrame = ((gifFrames.lastIndex) * ctx.progress).roundToInt()
        val frame = gifFrames[gifFrame]

        points.forEachIndexed { i, it ->
            val x = (frame.width * (it.x / ctx.stage.width)).roundToInt()
            val y = (frame.height * (it.y / ctx.stage.height)).roundToInt()
            val gifPixel = frame.getRGB(x, y)

            FillUtils.fill(ctx, i, 1f, Color(gifPixel), ignoreReverse = true)
        }
    }
}
