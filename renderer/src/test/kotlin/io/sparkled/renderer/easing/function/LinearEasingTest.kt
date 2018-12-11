package io.sparkled.renderer.easing.function

import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.easing.EasingTypeCode
import io.sparkled.model.animation.param.ParamCode
import io.sparkled.model.util.ArgumentUtils.arg
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.number.IsCloseTo.closeTo
import org.junit.jupiter.api.Test

class LinearEasingTest {

    @Test
    fun can_ease_0_to_100() {
        val linearEasing = LinearEasing()
        val expectedResults = floatArrayOf(0f, .1f, .2f, .3f, .4f, .5f, .6f, .7f, .8f, .9f, 1f)
        val testCount = expectedResults.size

        val easing = Easing(EasingTypeCode.LINEAR, listOf(
            arg(ParamCode.PERCENT_FROM, 0f),
            arg(ParamCode.PERCENT_TO, 100f)
        ))

        for (i in 0 until testCount) {
            val progress = linearEasing.getProgress(easing, i, testCount)
            assertThat(progress.toDouble(), closeTo(expectedResults[i].toDouble(), 0.001))
        }
    }

    @Test
    fun can_ease_100_to_0() {
        val linearEasing = LinearEasing()
        val expectedResults = floatArrayOf(1f, .9f, .8f, .7f, .6f, .5f, .4f, .3f, .2f, .1f, 0f)
        val testCount = expectedResults.size

        val easing = Easing(EasingTypeCode.LINEAR, listOf(
            arg(ParamCode.PERCENT_FROM, 100f),
            arg(ParamCode.PERCENT_TO, 0f)
        ))

        for (i in 0 until testCount) {
            val progress = linearEasing.getProgress(easing, i, testCount)
            assertThat(progress.toDouble(), closeTo(expectedResults[i].toDouble(), 0.001))
        }
    }

    @Test
    fun can_ease_20_to_80() {
        val linearEasing = LinearEasing()
        val expectedResults = floatArrayOf(.2f, .26f, .32f, .38f, .44f, .5f, .56f, .62f, .68f, .74f, .8f)
        val testCount = expectedResults.size

        val easing = Easing(EasingTypeCode.LINEAR, listOf(
            arg(ParamCode.PERCENT_FROM, 20f),
            arg(ParamCode.PERCENT_TO, 80f)
        ))

        for (i in 0 until testCount) {
            val progress = linearEasing.getProgress(easing, i, testCount)
            assertThat(progress.toDouble(), closeTo(expectedResults[i].toDouble(), 0.001))
        }
    }

    @Test
    fun can_ease_80_to_20() {
        val linearEasing = LinearEasing()
        val expectedResults = floatArrayOf(.8f, .74f, .68f, .62f, .56f, .5f, .44f, .38f, .32f, .26f, .2f)
        val testCount = expectedResults.size

        val easing = Easing(EasingTypeCode.LINEAR, listOf(
            arg(ParamCode.PERCENT_FROM, 80f),
            arg(ParamCode.PERCENT_TO, 20f)
        ))

        for (i in 0 until testCount) {
            val progress = linearEasing.getProgress(easing, i, testCount)
            assertThat(progress.toDouble(), closeTo(expectedResults[i].toDouble(), 0.001))
        }
    }
}
