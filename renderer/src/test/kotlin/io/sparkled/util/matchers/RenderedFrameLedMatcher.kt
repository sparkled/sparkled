package io.sparkled.util.matchers

/**
 * Convenience matcher for verifying the LEDs in a rendered frame.
 */
// TODO
//class RenderedFrameLedMatcher(private val leds: IntArray) : TypeSafeMatcher<RenderedFrame>() {
//
//    override fun describeTo(description: Description) {
//        val value = LedTestUtils.toLedString(leds)
//        description.appendText("is ").appendValue(value)
//    }
//
//    override fun describeMismatchSafely(frame: RenderedFrame, mismatchDescription: Description) {
//        val value = LedTestUtils.toLedString(frame)
//        mismatchDescription.appendText("was ").appendValue(value)
//    }
//
//    public override fun matchesSafely(frame: RenderedFrame): Boolean {
//        return LedTestUtils.toLedString(leds) == LedTestUtils.toLedString(frame)
//    }
//}
