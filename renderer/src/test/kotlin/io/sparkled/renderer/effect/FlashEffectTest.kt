package io.sparkled.renderer.effect

import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.animation.fill.BlendMode
import io.sparkled.model.animation.fill.Fill
import io.sparkled.model.util.ArgumentUtils.arg
import io.sparkled.renderer.easing.function.LinearEasing
import io.sparkled.renderer.fill.SingleColorFill
import io.sparkled.util.RenderUtils
import io.sparkled.util.matchers.SparkledMatchers.hasRenderedFrames
import kotlin.intArrayOf as f
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class FlashEffectTest {

    @Test
    fun can_render() {
        val effect = Effect(
            endFrame = 10,
            type = FlashEffect.id,
            easing = Easing(LinearEasing.id),
            fill = Fill(
                SingleColorFill.id,
                BlendMode.NORMAL,
                mapOf(
                    arg(SingleColorFill.Params.COLOR.name, "#ffffff")
                )
            )
        )

        val renderedStagePropData = RenderUtils.render(effect, effect.endFrame + 1, 10)

        assertThat(
            renderedStagePropData, hasRenderedFrames(
                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000),
                f(0x333333, 0x333333, 0x333333, 0x333333, 0x333333, 0x333333, 0x333333, 0x333333, 0x333333, 0x333333),
                f(0x666666, 0x666666, 0x666666, 0x666666, 0x666666, 0x666666, 0x666666, 0x666666, 0x666666, 0x666666),
                f(0x999999, 0x999999, 0x999999, 0x999999, 0x999999, 0x999999, 0x999999, 0x999999, 0x999999, 0x999999),
                f(0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC),
                f(0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF),
                f(0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC, 0xCCCCCC),
                f(0x999999, 0x999999, 0x999999, 0x999999, 0x999999, 0x999999, 0x999999, 0x999999, 0x999999, 0x999999),
                f(0x666666, 0x666666, 0x666666, 0x666666, 0x666666, 0x666666, 0x666666, 0x666666, 0x666666, 0x666666),
                f(0x333333, 0x333333, 0x333333, 0x333333, 0x333333, 0x333333, 0x333333, 0x333333, 0x333333, 0x333333),
                f(0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000)
            )
        )
    }
}
