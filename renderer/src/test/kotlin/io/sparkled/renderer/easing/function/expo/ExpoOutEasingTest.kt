package io.sparkled.renderer.easing.function.expo

import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.easing.EasingTypeCode
import io.sparkled.renderer.easing.function.ExpoOutEasing
import io.sparkled.util.matchers.SparkledMatchers.equalsFloatArray
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class ExpoOutEasingTest {

    @Test
    fun can_ease_0_to_100() {
        val easing = Easing(ExpoOutEasing.id, 0f, 100f)

        val expected = floatArrayOf(0f, 0.5f, 0.75f, 0.875f, 0.938f, 0.969f, 0.984f, 0.992f, 0.996f, 0.998f, 1f)
        val testCount = expected.size

        val actual = (0 until testCount).map { ExpoOutEasing.getScaledProgress(easing, it, testCount) }.toFloatArray()
        assertThat(actual, equalsFloatArray(expected))
    }

    @Test
    fun can_ease_100_to_0() {
        val easing = Easing(ExpoOutEasing.id, 100f, 0f)

        val expected = floatArrayOf(1f, 0.5f, 0.25f, 0.125f, 0.063f, 0.031f, 0.016f, 0.008f, 0.004f, 0.002f, 0f)
        val testCount = expected.size

        val actual = (0 until testCount).map { ExpoOutEasing.getScaledProgress(easing, it, testCount) }.toFloatArray()
        assertThat(actual, equalsFloatArray(expected))
    }

    @Test
    fun can_ease_20_to_80() {
        val easing = Easing(ExpoOutEasing.id, 20f, 80f)

        val expected = floatArrayOf(0.2f, 0.5f, 0.65f, 0.725f, 0.762f, 0.781f, 0.791f, 0.795f, 0.798f, 0.799f, 0.8f)
        val testCount = expected.size

        val actual = (0 until testCount).map { ExpoOutEasing.getScaledProgress(easing, it, testCount) }.toFloatArray()
        assertThat(actual, equalsFloatArray(expected))
    }

    @Test
    fun can_ease_80_to_20() {
        val easing = Easing(ExpoOutEasing.id, 80f, 20f)

        val expected = floatArrayOf(0.8f, 0.5f, 0.35f, 0.275f, 0.238f, 0.219f, 0.209f, 0.205f, 0.202f, 0.201f, 0.2f)
        val testCount = expected.size

        val actual = (0 until testCount).map { ExpoOutEasing.getScaledProgress(easing, it, testCount) }.toFloatArray()
        assertThat(actual, equalsFloatArray(expected))
    }
}
