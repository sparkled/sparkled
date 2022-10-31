package io.sparkled.renderer.easing.function.expo

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.floats.plusOrMinus
import io.kotest.matchers.shouldBe
import io.sparkled.model.animation.easing.Easing
import io.sparkled.renderer.easing.function.ExpoOutEasing

class ExpoOutEasingTest : StringSpec() {
    init {
        "can ease 0 to 100" {
            val easing = Easing(ExpoOutEasing.id, 0f, 100f)

            val expected = floatArrayOf(0f, 0.5f, 0.75f, 0.875f, 0.938f, 0.969f, 0.984f, 0.992f, 0.996f, 0.998f, 1f)
            val testCount = expected.size

            val actual = (0 until testCount).map { ExpoOutEasing.getScaledProgress(easing, it, testCount) }.toFloatArray()
            actual.zip(expected).forEach {(a, e) ->
                a shouldBe (e plusOrMinus 0.001f)
            }
        }

        "can ease 100 to 0" {
            val easing = Easing(ExpoOutEasing.id, 100f, 0f)

            val expected = floatArrayOf(1f, 0.5f, 0.25f, 0.125f, 0.063f, 0.031f, 0.016f, 0.008f, 0.004f, 0.002f, 0f)
            val testCount = expected.size

            val actual = (0 until testCount).map { ExpoOutEasing.getScaledProgress(easing, it, testCount) }.toFloatArray()
            actual.zip(expected).forEach {(a, e) ->
                a shouldBe (e plusOrMinus 0.001f)
            }
        }

        "can ease 20 to 80" {
            val easing = Easing(ExpoOutEasing.id, 20f, 80f)

            val expected = floatArrayOf(0.2f, 0.5f, 0.65f, 0.725f, 0.762f, 0.781f, 0.791f, 0.795f, 0.798f, 0.799f, 0.8f)
            val testCount = expected.size

            val actual = (0 until testCount).map { ExpoOutEasing.getScaledProgress(easing, it, testCount) }.toFloatArray()
            actual.zip(expected).forEach {(a, e) ->
                a shouldBe (e plusOrMinus 0.001f)
            }
        }

        "can ease 80 to 20" {
            val easing = Easing(ExpoOutEasing.id, 80f, 20f)

            val expected = floatArrayOf(0.8f, 0.5f, 0.35f, 0.275f, 0.238f, 0.219f, 0.209f, 0.205f, 0.202f, 0.201f, 0.2f)
            val testCount = expected.size

            val actual = (0 until testCount).map { ExpoOutEasing.getScaledProgress(easing, it, testCount) }.toFloatArray()
            actual.zip(expected).forEach {(a, e) ->
                a shouldBe (e plusOrMinus 0.001f)
            }
        }
    }
}
