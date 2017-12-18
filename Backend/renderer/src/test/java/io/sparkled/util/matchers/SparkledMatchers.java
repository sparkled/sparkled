package io.sparkled.util.matchers;

import io.sparkled.model.render.RenderedChannel;
import io.sparkled.model.render.RenderedFrame;
import org.hamcrest.Matcher;

public class SparkledMatchers {
    public static Matcher<RenderedChannel> hasLeds(int[][] leds) {
        return new RenderedChannelLedMatcher(leds);
    }

    public static Matcher<RenderedFrame> hasLeds(int[] leds) {
        return new RenderedFrameLedMatcher(leds);
    }
}
