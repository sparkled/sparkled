package net.chrisparton.sparkled.renderer.util;

import java.awt.*;

public class ColorUtils {

    private ColorUtils() {
    }

    public static Color adjustBrightness(Color color, float brightness) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hsb[2] *= brightness;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }
}
