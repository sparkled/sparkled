package net.chrisparton.sparkled.util;

import net.chrisparton.sparkled.model.render.Led;
import net.chrisparton.sparkled.model.render.RenderedChannel;
import net.chrisparton.sparkled.model.render.RenderedFrame;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Convenience methods for working with LEDs in tests.
 */
public class LedTestUtils {
    private LedTestUtils() {
    }

    public static String toLedString(RenderedChannel channel) {
        return channel.getFrames()
                .stream()
                .map(LedTestUtils::toLedString)
                .collect(Collectors.joining("\n"));
    }

    public static String toLedString(int[][] leds) {
        return Arrays.stream(leds)
                .map(LedTestUtils::toLedString)
                .collect(Collectors.joining("\n"));
    }

    public static String toLedString(RenderedFrame frame) {
        return frame.getLeds()
                .stream()
                .map(Led::toString)
                .collect(Collectors.joining(", "));
    }

    public static String toLedString(int[] leds) {
        return Arrays.stream(leds)
                .mapToObj(LedTestUtils::getLedFromRgb)
                .map(Led::toString)
                .collect(Collectors.joining(", "));
    }

    private static Led getLedFromRgb(int rgb) {
        byte[] ledData = new byte[3];
        Led led = new Led(ledData, 0);
        led.addRgb((rgb & 0xFF0000) >> 16, (rgb & 0x00FF00) >> 8, (rgb & 0x0000FF));
        return led;
    }
}
