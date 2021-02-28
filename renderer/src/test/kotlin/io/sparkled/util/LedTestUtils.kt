package io.sparkled.util

import io.sparkled.model.render.Led
import io.sparkled.model.render.RenderedFrame
import io.sparkled.model.render.RenderedStagePropData

/**
 * Convenience methods for working with LEDs in tests.
 */
object LedTestUtils {

    fun toLedString(channel: RenderedStagePropData): String {
        return channel.frames.map { toLedString(it) }.joinToString("\n")
    }

    fun toLedString(leds: Array<IntArray>): String {
        return leds.map { toLedString(it) }.joinToString("\n")
    }

    fun toLedString(frame: RenderedFrame): String {
        return (0 until frame.ledCount).map { frame.getLed(it) }.joinToString(", ", transform = Led::toString)
    }

    fun toLedString(leds: IntArray): String {
        return leds.map(this::getLedFromRgb).joinToString(", ", transform = Led::toString)
    }

    private fun getLedFromRgb(rgb: Int): Led {
        val ledData = ByteArray(3)
        val led = Led(ledData, 0, 0)
        led.addRgb(rgb and 0xFF0000 shr 16, rgb and 0x00FF00 shr 8, rgb and 0x0000FF)
        return led
    }
}
