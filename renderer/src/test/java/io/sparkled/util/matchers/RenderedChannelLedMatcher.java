package io.sparkled.util.matchers;

import io.sparkled.model.render.RenderedChannel;
import io.sparkled.model.render.RenderedFrame;
import io.sparkled.util.LedTestUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.List;

/**
 * Convenience matcher for verifying the LEDs in the rendered frames of a rendered channel.
 */
public class RenderedChannelLedMatcher extends TypeSafeMatcher<RenderedChannel> {

    private int[][] ledFrames;

    RenderedChannelLedMatcher(int[][] ledFrames) {
        this.ledFrames = ledFrames;
    }

    @Override
    public void describeTo(final Description description) {
        String value = LedTestUtils.toLedString(ledFrames);
        description.appendText("is ").appendValue(value);
    }

    @Override
    protected void describeMismatchSafely(final RenderedChannel channel, final Description mismatchDescription) {
        String value = LedTestUtils.toLedString(channel);
        mismatchDescription.appendText("was ").appendValue(value);
    }

    @Override
    protected boolean matchesSafely(final RenderedChannel channel) {
        final List<RenderedFrame> frames = channel.getFrames();
        if (frames.size() != ledFrames.length) {
            return false;
        }

        for (int i = 0; i < frames.size(); i++) {
            RenderedFrame renderedFrame = frames.get(i);

            if (!new RenderedFrameLedMatcher(ledFrames[i]).matchesSafely(renderedFrame)) {
                return false;
            }
        }

        return true;
    }
}
