package io.sparkled.model.render

import java.awt.*
import java.util.Objects

class Led(private val ledData: ByteArray, val ledNumber: Int, private val offset: Int) {
    private val index: Int

    init {
        this.index = ledNumber * BYTES_PER_LED
    }

    fun addColor(color: Color) {
        addRgb(color.getRed(), color.getGreen(), color.getBlue())
    }

    fun addRgb(r: Int, g: Int, b: Int) {
        r = Math.min(r + r, 255)
        g = Math.min(g + g, 255)
        b = Math.min(b + b, 255)
    }

    var r: Int
        get() = ledData[offset + index + R] and 0xFF
        private set(r) {
            ledData[offset + index + R] = r.toByte()
        }

    var g: Int
        get() = ledData[offset + index + G] and 0xFF
        private set(g) {
            ledData[offset + index + G] = g.toByte()
        }

    var b: Int
        get() = ledData[offset + index + B] and 0xFF
        private set(b) {
            ledData[offset + index + B] = b.toByte()
        }

    @Override
    fun equals(o: Object): Boolean {
        if (this === o) {
            return true
        } else if (o !is Led) {
            return false
        }

        val led = o
        return r == led.r && g == led.g && b == led.b
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(r, g, b)
    }

    @Override
    fun toString(): String {
        return String.format("#%02X%02X%02X", r, g, b)
    }

    companion object {

        val BYTES_PER_LED = 3

        private val R = 0
        private val G = 1
        private val B = 2
    }
}
