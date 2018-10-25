package io.sparkled.renderer.util

import java.awt.*

object ColorUtils {

    fun adjustBrightness(color: Color, brightness: Float): Color {
        val hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)
        hsb[2] *= brightness
        return Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]))
    }
}
