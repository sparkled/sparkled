package io.sparkled.renderer.easing.function

import io.sparkled.model.animation.easing.Easing
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.number.IsCloseTo.closeTo
import org.junit.jupiter.api.Test

class LinearEasingTest {

    @Test
    fun can_render() {
        val linearEasing = LinearEasing()
        val expectedResults = floatArrayOf(0f, .1f, .2f, .3f, .4f, .5f, .6f, .7f, .8f, .9f, 1f)
        val testCount = expectedResults.size

        for (i in 0 until testCount) {
            val progress = linearEasing.getProgress(Easing(), i, testCount)
            assertThat(progress.toDouble(), closeTo(expectedResults[i].toDouble(), 0.01))
        }
    }
}
