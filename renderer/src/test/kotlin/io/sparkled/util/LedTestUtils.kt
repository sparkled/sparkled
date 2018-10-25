package io.sparkled.util;

import io.sparkled.model.render.Led;
import io.sparkled.model.render.RenderedFrame;
import io.sparkled.model.render.RenderedStagePropData;

import java.util.Arrays;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

/**
 * Convenience methods for working with LEDs in tests.
 */
public class LedTestUtils {
    private LedTestUtils() {
    }

    public static String toLedString(RenderedStagePropData channel) {
        return channel.getFrames()
                .stream()
                .map(LedTestUtils::toLedString)
                .collect(joining("\n"));
    }

    public static String toLedString(int[][] leds) {
        return Arrays.stream(leds)
                .map(LedTestUtils::toLedString)
                .collect(joining("\n"));
    }

    public static String toLedString(RenderedFrame frame) {
        return IntStream.range(0, frame.getLedCount())
                .mapToObj(frame::getLed)
                .map(Led::toString)
                .collect(joining(", "));
    }

    public static String toLedString(int[] leds) {
        return Arrays.stream(leds)
                .mapToObj(LedTestUtils::getLedFromRgb)
                .map(Led::toString)
                .collect(joining(", "));
    }

    private static Led getLedFromRgb(int rgb) {
        byte[] ledData = new byte[3];
        Led led = new Led(ledData, 0, 0);
        led.addRgb((rgb & 0xFF0000) >> 16, (rgb & 0x00FF00) >> 8, (rgb & 0x0000FF));
        return led;
    }
}
