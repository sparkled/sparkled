package io.sparkled.util.matchers;

import io.sparkled.model.render.RenderedFrame;
import io.sparkled.model.render.RenderedStagePropData;
import org.hamcrest.Matcher;

public class SparkledMatchers {
    public static Matcher<RenderedStagePropData> hasLeds(int[][] leds) {
        return new RenderedStagePropDataLedMatcher(leds);
    }

    public static Matcher<RenderedFrame> hasLeds(int[] leds) {
        return new RenderedFrameLedMatcher(leds);
    }
}
