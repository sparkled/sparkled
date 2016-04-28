package net.chrisparton.sparkled.renderer.util;

import java.awt.*;

public class ColorUtils {

    private ColorUtils() {
    }

    public static Color adjustBrightness(Color color, double brightnessPercentage) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hsb[2] *= brightnessPercentage / 100;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }
}
