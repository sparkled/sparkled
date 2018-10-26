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
import io.sparkled.util.matchers.SparkledMatchers.hasLeds
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class FlashEffectRendererTest {

    @Test
    fun can_render() {
        val effect = Effect()
                .setType(EffectTypeCode.FLASH)
                .setEasing(Easing().setType(EasingTypeCode.LINEAR))
                .setFill(Fill()
                        .setType(FillTypeCode.SOLID)
                        .setParams(arrayListOf(
                                Param().setName(ParamName.COLOR).setValue("#ffffff")
                        ))
                )

        val renderedStagePropData = RenderUtils.render(effect, 11, 10)

        val c = intArrayOf(0x000000, 0x333333, 0x666666, 0x999999, 0xCCCCCC, 0xFFFFFF)
        assertThat(renderedStagePropData, hasLeds(arrayOf(
                intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]),
                intArrayOf(c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]),
                intArrayOf(c[2], c[2], c[2], c[2], c[2], c[2], c[2], c[2], c[2], c[2]),
                intArrayOf(c[3], c[3], c[3], c[3], c[3], c[3], c[3], c[3], c[3], c[3]),
                intArrayOf(c[4], c[4], c[4], c[4], c[4], c[4], c[4], c[4], c[4], c[4]),
                intArrayOf(c[5], c[5], c[5], c[5], c[5], c[5], c[5], c[5], c[5], c[5]),
                intArrayOf(c[4], c[4], c[4], c[4], c[4], c[4], c[4], c[4], c[4], c[4]),
                intArrayOf(c[3], c[3], c[3], c[3], c[3], c[3], c[3], c[3], c[3], c[3]),
                intArrayOf(c[2], c[2], c[2], c[2], c[2], c[2], c[2], c[2], c[2], c[2]),
                intArrayOf(c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1], c[1]),
                intArrayOf(c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0], c[0]))
        ))
    }
}
