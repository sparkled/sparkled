package io.sparkled.renderer.effect.line

import io.kotest.core.spec.style.StringSpec
import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.animation.fill.BlendMode
import io.sparkled.model.animation.fill.Fill
import io.sparkled.model.util.ArgumentUtils.arg
import io.sparkled.renderer.easing.function.LinearEasing
import io.sparkled.renderer.fill.SingleColorFill
import io.sparkled.util.RenderUtils

class BuildLineEffectTest : StringSpec() {

    init {
        "can render 4 segment build line on 10 led channel" {
            val effect = Effect(
                endFrame = 19,
                type = BuildLineEffect.id,
                args = mapOf(
                    arg("segments", 4)
                ),
                easing = Easing(LinearEasing.id),
                fill = Fill(
                    SingleColorFill.id,
                    BlendMode.NORMAL,
                    mapOf(
                        arg("color", "#ff0000")
                    )
                )
            )

            val renderedStagePropData = RenderUtils.render(effect, effect.endFrame + 1, 10)
// TODO
//            assertThat(
//                renderedStagePropData, hasRenderedFrames(
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0xFF0000, 0xFF0000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0xFF0000, 0xFF0000, 0xFF0000, 0x000000),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0xFF0000, 0xFF0000, 0xFF0000, 0x000000, 0x000000, 0x000000),
//                f(0x000000, 0x000000, 0xFF0000, 0xFF0000, 0xFF0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0xFF0000, 0xFF0000, 0xFF0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0xFF0000, 0xFF0000, 0xFF0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0xFF0000, 0xFF0000, 0xFF0000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0xFF0000, 0xFF0000),
//                f(0xFF0000, 0xFF0000, 0xFF0000, 0x000000, 0x000000, 0x000000, 0xFF0000, 0xFF0000, 0xFF0000, 0x000000),
//                f(0xFF0000, 0xFF0000, 0xFF0000, 0x000000, 0xFF0000, 0xFF0000, 0xFF0000, 0x000000, 0x000000, 0x000000),
//                f(0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0x000000, 0x000000, 0x000000, 0xFF0000),
//                f(0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0x000000, 0xFF0000, 0xFF0000, 0xFF0000),
//                f(0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0x000000),
//                f(0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0x000000),
//                f(0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0x000000),
//                f(0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0x000000),
//                f(0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000, 0xFF0000)
//            )
//            )
        }
    }
}
