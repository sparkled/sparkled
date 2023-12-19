package io.sparkled.renderer.effect

import io.kotest.core.spec.style.StringSpec
import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.animation.fill.BlendMode
import io.sparkled.model.animation.fill.Fill
import io.sparkled.model.util.ArgumentUtils.arg
import io.sparkled.renderer.easing.function.LinearEasing
import io.sparkled.renderer.fill.SingleColorFill
import io.sparkled.util.RenderUtils

class FlashEffectTest : StringSpec() {
    init {
        "can render" {
            val effect = Effect(
                endFrame = 10,
                type = FlashEffect.id,
                easing = Easing(LinearEasing.id),
                fill = Fill(
                    SingleColorFill.id,
                    BlendMode.NORMAL,
                    mapOf(
                        arg("color", "#ffffff")
                    )
                )
            )

            val renderedStagePropData = RenderUtils.render(effect, effect.endFrame + 1, 10)
// TODO
//        assertThat(
//            renderedStagePropData, hasRenderedFrames(
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
//                f(0x333333, 0x333333, 0x333333, 0x333333, 0x333333, 0x333333, 0x333333, 0x333333, 0x333333, 0x333333),
//                f(0x666666, 0x666666, 0x666666, 0x666666, 0x666666, 0x666666, 0x666666, 0x666666, 0x666666, 0x666666),
//                f(0x999999, 0x999999, 0x999999, 0x999999, 0x999999, 0x999999, 0x999999, 0x999999, 0x999999, 0x999999),
//                f(0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC),
//                f(0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF),
//                f(0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC),
//                f(0x999999, 0x999999, 0x999999, 0x999999, 0x999999, 0x999999, 0x999999, 0x999999, 0x999999, 0x999999),
//                f(0x666666, 0x666666, 0x666666, 0x666666, 0x666666, 0x666666, 0x666666, 0x666666, 0x666666, 0x666666),
//                f(0x333333, 0x333333, 0x333333, 0x333333, 0x333333, 0x333333, 0x333333, 0x333333, 0x333333, 0x333333),
//                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000)
//            )
//        )
        }
    }
}
