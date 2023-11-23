package io.sparkled.renderer.effect

import io.sparkled.model.animation.param.Param
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SemVer
import io.sparkled.renderer.api.StatelessSparkledEffect
import io.sparkled.renderer.util.FillUtils
import io.sparkled.renderer.util.ParamUtils
import java.awt.Color
import kotlin.math.roundToInt

object GifEffect : StatelessSparkledEffect {

    enum class Params { FILE_NAME }

    override val id = "@sparkled/gif"
    override val name = "Gif"
    override val version = SemVer(1, 0, 0)
    override val params = listOf(
        Param.string(Params.FILE_NAME.name, "Filename", "")
    )

    override fun render(ctx: RenderContext) {
        val points = ctx.stageProp.ledPositions

        val gifFrames = ctx.loadGif(ParamUtils.getString(ctx.effect, Params.FILE_NAME.name))

        val gifFrame = ((gifFrames.size - 1) * ctx.progress).roundToInt()
        val frame = gifFrames[gifFrame]

        points.forEachIndexed { i, it ->
            val x = (frame.width * (it.x / ctx.stage.width)).roundToInt()
            val y = (frame.height * (it.y / ctx.stage.height)).roundToInt()
            val gifPixel = frame.getRGB(x, y)

            FillUtils.fill(ctx, i, 1f, Color(gifPixel), ignoreReverse = true)
        }
    }
}
