package io.sparkled.util.matchers

import io.sparkled.model.render.RenderedStagePropData
import io.sparkled.util.LedTestUtils
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Convenience matcher for verifying the LEDs in the rendered frames of a rendered stage prop.
 */
class RenderedStagePropDataLedMatcher internal constructor(private val ledFrames: Array<IntArray>) : TypeSafeMatcher<RenderedStagePropData>() {

    override fun describeTo(description: Description) {
        val value = LedTestUtils.toLedString(ledFrames)
        description.appendText("is ").appendValue(value)
    }

    override fun describeMismatchSafely(channel: RenderedStagePropData, mismatchDescription: Description) {
        val value = LedTestUtils.toLedString(channel)
        mismatchDescription.appendText("was ").appendValue(value)
    }

    override fun matchesSafely(channel: RenderedStagePropData): Boolean {
        val frames = channel.frames
        if (frames.size != ledFrames.size) {
            return false
        }

        for (i in 0 until frames.size) {
            val renderedFrame = frames[i]

            if (!RenderedFrameLedMatcher(ledFrames[i]).matchesSafely(renderedFrame)) {
                return false
            }
        }

        return true
    }
}
