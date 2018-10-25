package io.sparkled.renderer.util

import java.awt.Color

object ColorUtils {

    fun adjustBrightness(color: Color, brightness: Float): Color {
        val hsb = Color.RGBtoHSB(color.red, color.green, color.blue, null)
        return Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2] * brightness))
    }
}
