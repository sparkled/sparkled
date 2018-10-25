package io.sparkled.util

import io.sparkled.model.render.Led
import io.sparkled.model.render.RenderedFrame
import io.sparkled.model.render.RenderedStagePropData

import java.util.Arrays
import java.util.stream.IntStream

import java.util.stream.Collectors.joining

/**
 * Convenience methods for working with LEDs in tests.
 */
object LedTestUtils {

    fun toLedString(channel: RenderedStagePropData): String {
        return channel.getFrames()
                .stream()
                .map(???({ toLedString() }))
        .collect(joining("\n"))
    }

    fun toLedString(leds: Array<IntArray>): String {
        return Arrays.stream(leds)
                .map(???({ toLedString() }))
        .collect(joining("\n"))
    }

    fun toLedString(frame: RenderedFrame): String {
        return IntStream.range(0, frame.getLedCount())
                .mapToObj(???({ frame.getLed() }))
        .map(???({ Led.toString() }))
        .collect(joining(", "))
    }

    fun toLedString(leds: IntArray): String {
        return Arrays.stream(leds)
                .mapToObj(???({ getLedFromRgb(it) }))
        .map(???({ Led.toString() }))
        .collect(joining(", "))
    }

    private fun getLedFromRgb(rgb: Int): Led {
        val ledData = ByteArray(3)
        val led = Led(ledData, 0, 0)
        led.addRgb(rgb and 0xFF0000 shr 16, rgb and 0x00FF00 shr 8, rgb and 0x0000FF)
        return led
    }
}
