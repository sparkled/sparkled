package io.sparkled.renderer.easing.function

import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.easing.EasingTypeCode
import io.sparkled.model.animation.param.ParamCode
import io.sparkled.model.util.ArgumentUtils.arg
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.number.IsCloseTo.closeTo
import org.junit.jupiter.api.Test

class ConstantEasingTest {

    @Test
    fun can_ease_constant_50() {
        val constantEasing = ConstantEasing()
        val expectedResults = floatArrayOf(.5f, .5f, .5f, .5f, .5f, .5f, .5f, .5f, .5f, .5f, .5f)
        val testCount = expectedResults.size

        val easing = Easing(EasingTypeCode.CONSTANT, listOf(
            arg(ParamCode.PERCENT, 50f)
        ))

        for (i in 0 until testCount) {
            val progress = constantEasing.getProgress(easing, i, testCount)
            assertThat(progress.toDouble(), closeTo(expectedResults[i].toDouble(), 0.001))
        }
    }
}
