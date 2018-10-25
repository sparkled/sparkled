package io.sparkled.renderer.effect

import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.easing.EasingTypeCode
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.animation.effect.EffectTypeCode
import io.sparkled.model.animation.fill.Fill
import io.sparkled.model.animation.fill.FillTypeCode
import io.sparkled.model.animation.param.Param
import io.sparkled.model.animation.param.ParamName
import io.sparkled.model.render.RenderedStagePropData
import io.sparkled.util.RenderUtils
import io.sparkled.util.matchers.SparkledMatchers
import org.junit.jupiter.api.Test

import org.hamcrest.MatcherAssert.assertThat

class LineEffectRendererTest {

    @Test
    fun can_render_1_led_line_on_10_led_channel() {
        val effect = Effect()
                .setType(EffectTypeCode.LINE)
                .setParams(
                        Param().setName(ParamName.LENGTH).setValue(1)
                )
                .setEasing(
                        Easing()
                                .setType(EasingTypeCode.LINEAR)
                )
                .setFill(
                        Fill()
                                .setType(FillTypeCode.SOLID)
                                .setParams(
                                        Param().setName(ParamName.COLOR).setValue("#ffffff")
                                )
                )

        val renderedStagePropData = RenderUtils.render(effect, 12, 10)

        val c = intArrayOf(0x000000, 0xFFFFFF)
        assertThat(renderedStagePropData, SparkledMatchers.hasLeds(arrayOf(intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[1], c[0], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[1], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[1], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]))))
    }

    @Test
    fun can_render_20_led_line_on_10_led_channel() {
        val effect = Effect()
                .setType(EffectTypeCode.LINE)
                .setParams(
                        Param().setName(ParamName.LENGTH).setValue(20)
                )
                .setEasing(
                        Easing()
                                .setType(EasingTypeCode.LINEAR)
                )
                .setFill(
                        Fill()
                                .setType(FillTypeCode.SOLID)
                                .setParams(
                                        Param().setName(ParamName.COLOR).setValue("#ffffff")
                                )
                )

        val renderedStagePropData = RenderUtils.render(effect, 20, 10)

        val c = intArrayOf(0x000000, 0xFFFFFF)
        assertThat(renderedStagePropData, SparkledMatchers.hasLeds(arrayOf(intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[1], c[1], c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0], c[0], c[0]), intArrayOf(c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]), intArrayOf(c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0]), intArrayOf(c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]), intArrayOf(c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]), intArrayOf(c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]), intArrayOf(c[0], c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[1]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1], c[1]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]))))
    }
}
