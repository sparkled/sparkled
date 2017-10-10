package net.chrisparton.sparkled.renderer.effect;

import net.chrisparton.sparkled.converter.ColourParamConverter;
import net.chrisparton.sparkled.model.animation.AnimationEffect;
import net.chrisparton.sparkled.renderdata.RenderedChannel;
import net.chrisparton.sparkled.renderdata.RenderedFrame;
import net.chrisparton.sparkled.renderer.util.ColorUtils;

import java.awt.*;

public class FlashEffectRenderer extends EffectRenderer {

    @Override
    public void render(RenderedChannel channel, RenderedFrame frame, AnimationEffect effect, int progressPercentage) {
        int startLed = 0;
        int endLed = channel.getLedCount();

        Color color = new ColourParamConverter().convert(effect);

        double progress = progressPercentage / 100.0 * Math.PI;
        double brightnessPercentage = 100.0 * Math.sin(progress);
        Color adjustedColor = ColorUtils.adjustBrightness(color, brightnessPercentage);

        for (int i = startLed; i < endLed; i++) {
            frame.getLed(i).addRgb(
                    adjustedColor.getRed(),
                    adjustedColor.getGreen(),
                    adjustedColor.getBlue()
            );
        }
    }
}
