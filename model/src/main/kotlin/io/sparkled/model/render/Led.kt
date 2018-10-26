package io.sparkled.model.render

import java.awt.Color
import java.util.Arrays

class Led(private val ledData: ByteArray, val ledNumber: Int, private val offset: Int) {
    private val index: Int

    init {
        this.index = ledNumber * BYTES_PER_LED
    }

    fun addColor(color: Color) = addRgb(color.red, color.green, color.blue)

    fun addRgb(r: Int, g: Int, b: Int) {
        this.r = Math.min(r + r, 255)
        this.g = Math.min(g + g, 255)
        this.b = Math.min(b + b, 255)
    }

    var r: Int
        get() = (ledData[offset + index + R]).toInt() and 0xFF
        private set(r) {
            ledData[offset + index + R] = r.toByte()
        }

    var g: Int
        get() = (ledData[offset + index + G]).toInt() and 0xFF
        private set(g) {
            ledData[offset + index + G] = g.toByte()
        }

    var b: Int
        get() = (ledData[offset + index + B]).toInt() and 0xFF
        private set(b) {
            ledData[offset + index + B] = b.toByte()
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Led

        if (!Arrays.equals(ledData, other.ledData)) return false
        if (ledNumber != other.ledNumber) return false
        if (offset != other.offset) return false
        if (index != other.index) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(ledData)
        result = 31 * result + ledNumber
        result = 31 * result + offset
        result = 31 * result + index
        return result
    }

    override fun toString(): String {
        return String.format("#%02X%02X%02X", r, g, b)
    }

    companion object {

        const val BYTES_PER_LED = 3

        private const val R = 0
        private const val G = 1
        private const val B = 2
    }
}
