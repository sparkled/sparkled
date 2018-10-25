package io.sparkled.renderer.easing.function

import io.sparkled.model.animation.easing.Easing
import org.junit.jupiter.api.Test

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.number.IsCloseTo.closeTo

class LinearEasingTest {

    @Test
    fun can_render() {
        val linearEasing = LinearEasing()
        val expectedResults = floatArrayOf(0f, .1f, .2f, .3f, .4f, .5f, .6f, .7f, .8f, .9f, 1f)
        val testCount = expectedResults.size

        for (i in 0..testCount - 1) {
            val progress = linearEasing.getProgress(Easing(), i, testCount)
            assertThat(progress, closeTo(expectedResults[i], 0.01))
        }
    }
}
