package io.sparkled.renderer

import io.kotest.core.spec.style.StringSpec
import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.animation.fill.BlendMode
import io.sparkled.model.animation.fill.Fill
import io.sparkled.model.util.ArgumentUtils.arg
import io.sparkled.renderer.easing.function.LinearEasing
import io.sparkled.renderer.effect.line.LineEffect
import io.sparkled.renderer.fill.SingleColorFill
import io.sparkled.util.RenderUtils

class ReverseStagePropRendererTest : StringSpec() {

    init {
        "can render_3 led line on reversed stage prop" {
            val effect = Effect(
                endFrame = 19,
                type = LineEffect.id,
                args = mapOf(
                    arg(LineEffect.Params.LENGTH.name, 3)
                ),
                easing = Easing(LinearEasing.id),
                fill = Fill(
                    SingleColorFill.id,
                    BlendMode.NORMAL,
                    mapOf(
                        arg(SingleColorFill.Params.COLOR.name, "#ff0000")
                    )
                )
            )

            val stageProp = StagePropModel(
                code = RenderUtils.PROP_CODE,
                id = RenderUtils.PROP_ID,
                ledCount = 10,
                reverse = true,
            )

            val renderedStagePropData = RenderUtils.render(mapOf(RenderUtils.PROP_ID to listOf(effect)), effect.endFrame + 1, listOf(stageProp))
// TODO
//        assertThat(
//            renderedStagePropData[RenderUtils.PROP_UUID.toString()]!!, hasRenderedFrames(
//            f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//            f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0xAE0000),
//            f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x5E0000, 0xFF0000),
//            f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x0D0000, 0xFF0000, 0xFF0000),
//            f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0xBC0000, 0xFF0000, 0xFF0000),
//            f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x6B0000, 0xFF0000, 0xFF0000, 0x940000),
//            f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x1B0000, 0xFF0000, 0xFF0000, 0xE40000, 0x000000),
//            f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0xC90000, 0xFF0000, 0xFF0000, 0x360000, 0x000000),
//            f(0x000000, 0x000000, 0x000000, 0x000000, 0x790000, 0xFF0000, 0xFF0000, 0x860000, 0x000000, 0x000000),
//            f(0x000000, 0x000000, 0x000000, 0x280000, 0xFF0000, 0xFF0000, 0xD70000, 0x000000, 0x000000, 0x000000),
//            f(0x000000, 0x000000, 0x000000, 0xD70000, 0xFF0000, 0xFF0000, 0x280000, 0x000000, 0x000000, 0x000000),
//            f(0x000000, 0x000000, 0x860000, 0xFF0000, 0xFF0000, 0x790000, 0x000000, 0x000000, 0x000000, 0x000000),
//            f(0x000000, 0x360000, 0xFF0000, 0xFF0000, 0xC90000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//            f(0x000000, 0xE40000, 0xFF0000, 0xFF0000, 0x1B0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//            f(0x940000, 0xFF0000, 0xFF0000, 0x6B0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//            f(0xFF0000, 0xFF0000, 0xBC0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//            f(0xFF0000, 0xFF0000, 0x0D0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//            f(0xFF0000, 0x5E0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//            f(0xAE0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//            f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000)
//        )
//        )
        }
    }
}
