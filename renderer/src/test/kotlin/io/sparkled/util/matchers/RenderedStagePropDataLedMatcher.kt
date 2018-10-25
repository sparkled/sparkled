package io.sparkled.util.matchers

import io.sparkled.model.render.RenderedFrame
import io.sparkled.model.render.RenderedStagePropData
import io.sparkled.util.LedTestUtils
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Convenience matcher for verifying the LEDs in the rendered frames of a rendered stage prop.
 */
class RenderedStagePropDataLedMatcher internal constructor(private val ledFrames: Array<IntArray>) : TypeSafeMatcher<RenderedStagePropData>() {

    @Override
    fun describeTo(description: Description) {
        val value = LedTestUtils.toLedString(ledFrames)
        description.appendText("is ").appendValue(value)
    }

    @Override
    protected fun describeMismatchSafely(channel: RenderedStagePropData, mismatchDescription: Description) {
        val value = LedTestUtils.toLedString(channel)
        mismatchDescription.appendText("was ").appendValue(value)
    }

    @Override
    protected fun matchesSafely(channel: RenderedStagePropData): Boolean {
        val frames = channel.getFrames()
        if (frames.size() !== ledFrames.size) {
            return false
        }

        for (i in 0..frames.size() - 1) {
            val renderedFrame = frames.get(i)

            if (!RenderedFrameLedMatcher(ledFrames[i]).matchesSafely(renderedFrame)) {
                return false
            }
        }

        return true
    }
}
