package net.chrisparton.sparkled.util.matchers;

import net.chrisparton.sparkled.renderdata.RenderedChannel;
import net.chrisparton.sparkled.renderdata.RenderedFrame;
import org.hamcrest.Matcher;

public class SparkledMatchers {
    public static Matcher<RenderedChannel> hasLeds(int[][] leds) {
        return new RenderedChannelLedMatcher(leds);
    }

    public static Matcher<RenderedFrame> hasLeds(int[] leds) {
        return new RenderedFrameLedMatcher(leds);
    }
}
