package net.chrisparton.sparkled.renderer.effect;

import net.chrisparton.sparkled.model.animation.AnimationEffect;
import net.chrisparton.sparkled.model.render.RenderedChannel;
import net.chrisparton.sparkled.model.render.RenderedFrame;
import net.chrisparton.sparkled.renderer.converter.ColourParamConverter;
import net.chrisparton.sparkled.renderer.util.ColorUtils;

import java.awt.*;

public class FlashEffectRenderer extends EffectRenderer {

    @Override
    public void render(RenderedChannel channel, RenderedFrame frame, AnimationEffect effect, double progress) {
        Color color = new ColourParamConverter().convert(effect);

        double brightnessPercentage = Math.sin(progress * Math.PI);
        Color adjustedColor = ColorUtils.adjustBrightness(color, brightnessPercentage);

        for (int i = 0; i < channel.getLedCount(); i++) {
            frame.getLed(i).addRgb(adjustedColor);
        }
    }
}
