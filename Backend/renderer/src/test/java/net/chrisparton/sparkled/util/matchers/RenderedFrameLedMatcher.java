package net.chrisparton.sparkled.util.matchers;

import net.chrisparton.sparkled.model.render.Led;
import net.chrisparton.sparkled.model.render.RenderedFrame;
import net.chrisparton.sparkled.util.LedTestUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Convenience matcher for verifying the LEDs in a rendered frame.
 */
public class RenderedFrameLedMatcher extends TypeSafeMatcher<RenderedFrame> {

    private int[] leds;

    RenderedFrameLedMatcher(int[] leds) {
        this.leds = leds;
    }

    @Override
    public void describeTo(final Description description) {
        String value = LedTestUtils.toLedString(leds);
        description.appendText("is ").appendValue(value);
    }

    @Override
    protected void describeMismatchSafely(final RenderedFrame frame, final Description mismatchDescription) {
        String value = LedTestUtils.toLedString(frame);
        mismatchDescription.appendText("was ").appendValue(value);
    }

    @Override
    protected boolean matchesSafely(final RenderedFrame frame) {
        if (frame.getLedCount() != leds.length) {
            return false;
        }

        for (int i = 0; i < frame.getLedCount(); i++) {
            Led led = frame.getLed(i);
            boolean rMatches = led.getR() == (leds[i] & 0xFF0000) >> 16;
            boolean gMatches = led.getG() == (leds[i] & 0x00FF00) >> 8;
            boolean bMatches = led.getB() == (leds[i] & 0x0000FF);

            if (!rMatches || !gMatches || !bMatches) {
                return false;
            }
        }

        return true;
    }
}
