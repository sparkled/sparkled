package net.chrisparton.sparkled.renderer.effect;

import net.chrisparton.sparkled.converter.ColourParamConverter;
import net.chrisparton.sparkled.entity.AnimationEffect;
import net.chrisparton.sparkled.renderer.data.RenderedChannel;
import net.chrisparton.sparkled.renderer.data.RenderedFrame;
import net.chrisparton.sparkled.renderer.util.ColorUtils;

import java.awt.*;

public class FillEffectRenderer extends EffectRenderer {

    @Override
    public void render(RenderedChannel channel, RenderedFrame frame, AnimationEffect effect, int progressPercentage) {
        int startLed = 0;
        int endLed = channel.getLedCount();

        Color color = new ColourParamConverter().convert(effect);

        frame.getLeds().forEach(led -> led.addRgb(color));
    }
}
