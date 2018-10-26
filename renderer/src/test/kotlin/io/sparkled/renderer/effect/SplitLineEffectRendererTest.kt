package io.sparkled.renderer.effect

import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.easing.EasingTypeCode
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.animation.effect.EffectTypeCode
import io.sparkled.model.animation.fill.Fill
import io.sparkled.model.animation.fill.FillTypeCode
import io.sparkled.model.animation.param.Param
import io.sparkled.model.animation.param.ParamName
import io.sparkled.util.RenderUtils
import io.sparkled.util.matchers.SparkledMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class SplitLineEffectRendererTest {

    @Test
    fun can_render_5_led_line_on_10_led_channel() {
        val effect = Effect()
                .setType(EffectTypeCode.SPLIT_LINE)
                .setParams(arrayListOf(
                        Param().setName(ParamName.LENGTH).setValue(5)
                ))
                .setEasing(
                        Easing()
                                .setType(EasingTypeCode.LINEAR)
                )
                .setFill(
                        Fill()
                                .setType(FillTypeCode.SOLID)
                                .setParams(arrayListOf(
                                        Param().setName(ParamName.COLOR).setValue("#ffffff")
                                ))
                )

        val renderedStagePropData = RenderUtils.render(effect, 50, 10)

        val c = intArrayOf(0x000000, 0xFFFFFF)
        assertThat(renderedStagePropData, SparkledMatchers.hasLeds(arrayOf(intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[1], c[1], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[1], c[1], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[1], c[1], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[1], c[1], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[1], c[1], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]), intArrayOf(c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]), intArrayOf(c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]), intArrayOf(c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]), intArrayOf(c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]), intArrayOf(c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0]), intArrayOf(c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0]), intArrayOf(c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0]), intArrayOf(c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0]), intArrayOf(c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0]), intArrayOf(c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[1], c[0], c[0], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[1], c[0], c[0], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[1], c[0], c[0], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[1], c[0], c[0], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[1], c[0], c[0], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]), intArrayOf(c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]), intArrayOf(c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]), intArrayOf(c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]), intArrayOf(c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]), intArrayOf(c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1]), intArrayOf(c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1]), intArrayOf(c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1]), intArrayOf(c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1]), intArrayOf(c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]))))
    }

    @Test
    fun can_render_5_led_line_on_11_led_channel() {
        val effect = Effect()
                .setType(EffectTypeCode.SPLIT_LINE)
                .setParams(arrayListOf(
                        Param().setName(ParamName.LENGTH).setValue(5)
                ))
                .setEasing(
                        Easing().setType(EasingTypeCode.LINEAR)
                )
                .setFill(
                        Fill()
                                .setType(FillTypeCode.SOLID)
                                .setParams(arrayListOf(
                                        Param().setName(ParamName.COLOR).setValue("#ffffff")
                                ))
                )

        val renderedStagePropData = RenderUtils.render(effect, 50, 11)

        val c = intArrayOf(0x000000, 0xFFFFFF)
        assertThat(renderedStagePropData, SparkledMatchers.hasLeds(arrayOf(intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[1], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[1], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[1], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[1], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[1], c[1], c[1], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[1], c[1], c[1], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[1], c[1], c[1], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[1], c[1], c[1], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[1], c[1], c[1], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]), intArrayOf(c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]), intArrayOf(c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]), intArrayOf(c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]), intArrayOf(c[0], c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0], c[0]), intArrayOf(c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0]), intArrayOf(c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0]), intArrayOf(c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0]), intArrayOf(c[0], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[0]), intArrayOf(c[1], c[1], c[1], c[1], c[1], c[0], c[1], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[1], c[1], c[0], c[1], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[1], c[1], c[0], c[1], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[1], c[1], c[0], c[1], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[1], c[0], c[0], c[0], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[1], c[0], c[0], c[0], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[1], c[0], c[0], c[0], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[1], c[0], c[0], c[0], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[1], c[0], c[0], c[0], c[1], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[1], c[1], c[1]), intArrayOf(c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]), intArrayOf(c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]), intArrayOf(c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]), intArrayOf(c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]), intArrayOf(c[1], c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1], c[1]), intArrayOf(c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1]), intArrayOf(c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1]), intArrayOf(c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1]), intArrayOf(c[1], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[1]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]), intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]))))
    }
}