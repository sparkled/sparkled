package io.sparkled.model.render

import io.sparkled.model.util.MathUtils.constrain
import io.sparkled.model.util.MathUtils.lerp
import java.awt.Color
import java.util.*
import kotlin.math.roundToInt

class Led(private val ledData: ByteArray, private val ledIndex: Int, private val offset: Int) {
    private val index: Int

    init {
        this.index = ledIndex * BYTES_PER_LED
    }

    fun setColor(color: Color, alpha: Float) {
        this.r = lerp(this.r.toFloat(), color.red.toFloat(), alpha).roundToInt()
        this.g = lerp(this.g.toFloat(), color.green.toFloat(), alpha).roundToInt()
        this.b = lerp(this.b.toFloat(), color.blue.toFloat(), alpha).roundToInt()
    }

    fun addColor(color: Color) = addRgb(color.red, color.green, color.blue)
    fun subtractColor(color: Color) = addRgb(-color.red, -color.green, -color.blue)
    fun maskColor(color: Color) = addRgb(-(255 - color.red), -(255 - color.green), -(255 - color.blue))

    fun addRgb(r: Int, g: Int, b: Int) {
        this.r = constrain(this.r + r, 0, 255)
        this.g = constrain(this.g + g, 0, 255)
        this.b = constrain(this.b + b, 0, 255)
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Led

        if (!Arrays.equals(ledData, other.ledData)) return false
        if (ledIndex != other.ledIndex) return false
        if (offset != other.offset) return false
        if (index != other.index) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(ledData)
        result = 31 * result + ledIndex
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
