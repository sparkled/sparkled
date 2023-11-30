package io.sparkled.model.render

import io.sparkled.model.util.MathUtils.lerp
import java.awt.Color
import kotlin.math.roundToInt

// TODO consider replacing this class with functions to save memory
data class Led(
    @Suppress("ArrayInDataClass") private val ledData: ByteArray,
    private val pixelIndex: Int,
    private val offset: Int,
) {
    private val index: Int = pixelIndex * BYTES_PER_LED

    fun setColor(color: Color, alpha: Float) {
        this.r = lerp(this.r.toFloat(), color.red.toFloat(), alpha).roundToInt()
        this.g = lerp(this.g.toFloat(), color.green.toFloat(), alpha).roundToInt()
        this.b = lerp(this.b.toFloat(), color.blue.toFloat(), alpha).roundToInt()
    }

    fun addColor(color: Color) = addRgb(color.red, color.green, color.blue)
    fun subtractColor(color: Color) = addRgb(-color.red, -color.green, -color.blue)
    fun maskColor(color: Color) = addRgb(-(255 - color.red), -(255 - color.green), -(255 - color.blue))

    fun setRgb(r: Int, g: Int, b: Int) {
        this.r = r.coerceIn(0, 255)
        this.g = g.coerceIn(0, 255)
        this.b = b.coerceIn(0, 255)
    }

    fun addRgb(r: Int, g: Int, b: Int) {
        this.r = (this.r + r).coerceIn(0, 255)
        this.g = (this.g + g).coerceIn(0, 255)
        this.b = (this.b + b).coerceIn(0, 255)
    }

    private var r: Int
        get() = (ledData[offset + index + R]).toInt() and 0xFF
        private set(r) {
            ledData[offset + index + R] = r.toByte()
        }

    private var g: Int
        get() = (ledData[offset + index + G]).toInt() and 0xFF
        private set(g) {
            ledData[offset + index + G] = g.toByte()
        }

    private var b: Int
        get() = (ledData[offset + index + B]).toInt() and 0xFF
        private set(b) {
            ledData[offset + index + B] = b.toByte()
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
