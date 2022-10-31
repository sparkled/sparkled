package io.sparkled.renderer.effect

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.sparkled.model.animation.param.Param
import io.sparkled.model.entity.v2.partial.Point2d
import io.sparkled.renderer.api.RenderContext
import io.sparkled.renderer.api.SemVer
import io.sparkled.renderer.api.SparkledEffect
import io.sparkled.renderer.util.FillUtils
import io.sparkled.renderer.util.ParamUtils
import java.awt.Color
import kotlin.math.roundToInt

object GifEffect : SparkledEffect<Unit> {

    enum class Params { FILE_NAME }

    override val id = "@sparkled/gif"
    override val name = "Gif"
    override val version = SemVer(1, 0, 0)
    override val params = listOf(
        Param.string(Params.FILE_NAME.name, "Filename", "")
    )

    override fun createState(ctx: RenderContext) {}

    override fun render(ctx: RenderContext, state: Unit) {
        val points = ObjectMapper().registerKotlinModule().readValue<List<Point2d>>(ctx.stageProp.ledPositionsJson)

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
