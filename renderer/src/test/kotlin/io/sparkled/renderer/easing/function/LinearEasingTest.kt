package io.sparkled.renderer.easing.function

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.floats.plusOrMinus
import io.kotest.matchers.shouldBe
import io.sparkled.model.animation.easing.Easing

class LinearEasingTest : StringSpec() {

    init {
        "can ease 0 to 100" {
            val easing = Easing(LinearEasing.id, 0f, 100f)
            val expected = floatArrayOf(0f, .1f, .2f, .3f, .4f, .5f, .6f, .7f, .8f, .9f, 1f)
            val testCount = expected.size

            val actual = (0 until testCount).map { LinearEasing.getScaledProgress(easing, it, testCount) }.toFloatArray()
            actual.zip(expected).forEach {(a, e) ->
                a shouldBe (e plusOrMinus 0.001f)
            }
        }

        "can ease 100 to 0" {
            val easing = Easing(LinearEasing.id, 100f, 0f)
            val expected = floatArrayOf(1f, .9f, .8f, .7f, .6f, .5f, .4f, .3f, .2f, .1f, 0f)
            val testCount = expected.size

            val actual = (0 until testCount).map { LinearEasing.getScaledProgress(easing, it, testCount) }.toFloatArray()
            actual.zip(expected).forEach {(a, e) ->
                a shouldBe (e plusOrMinus 0.001f)
            }
        }

        "can ease 20 to 80" {
            val easing = Easing(LinearEasing.id, 20f, 80f)
            val expected = floatArrayOf(.2f, .26f, .32f, .38f, .44f, .5f, .56f, .62f, .68f, .74f, .8f)
            val testCount = expected.size

            val actual = (0 until testCount).map { LinearEasing.getScaledProgress(easing, it, testCount) }.toFloatArray()
            actual.zip(expected).forEach {(a, e) ->
                a shouldBe (e plusOrMinus 0.001f)
            }
        }

        "can ease 80 to 20" {
            val easing = Easing(LinearEasing.id, 80f, 20f)
            val expected = floatArrayOf(.8f, .74f, .68f, .62f, .56f, .5f, .44f, .38f, .32f, .26f, .2f)
            val testCount = expected.size

            val actual = (0 until testCount).map { LinearEasing.getScaledProgress(easing, it, testCount) }.toFloatArray()
            actual.zip(expected).forEach {(a, e) ->
                a shouldBe (e plusOrMinus 0.001f)
            }
        }
    }
}
