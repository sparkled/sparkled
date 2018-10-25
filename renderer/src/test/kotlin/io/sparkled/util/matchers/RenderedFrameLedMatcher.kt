package io.sparkled.util.matchers

import io.sparkled.model.render.Led
import io.sparkled.model.render.RenderedFrame
import io.sparkled.util.LedTestUtils
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Convenience matcher for verifying the LEDs in a rendered frame.
 */
class RenderedFrameLedMatcher internal constructor(private val leds: IntArray) : TypeSafeMatcher<RenderedFrame>() {

    @Override
    fun describeTo(description: Description) {
        val value = LedTestUtils.toLedString(leds)
        description.appendText("is ").appendValue(value)
    }

    @Override
    protected fun describeMismatchSafely(frame: RenderedFrame, mismatchDescription: Description) {
        val value = LedTestUtils.toLedString(frame)
        mismatchDescription.appendText("was ").appendValue(value)
    }

    @Override
    protected fun matchesSafely(frame: RenderedFrame): Boolean {
        if (frame.getLedCount() !== leds.size) {
            return false
        }

        for (i in 0..frame.getLedCount() - 1) {
            val led = frame.getLed(i)
            val rMatches = led.getR() === leds[i] and 0xFF0000 shr 16
            val gMatches = led.getG() === leds[i] and 0x00FF00 shr 8
            val bMatches = led.getB() === leds[i] and 0x0000FF

            if (!rMatches || !gMatches || !bMatches) {
                return false
            }
        }

        return true
    }
}
