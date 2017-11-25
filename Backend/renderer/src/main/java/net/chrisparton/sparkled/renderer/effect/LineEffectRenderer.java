package net.chrisparton.sparkled.renderer.effect;

import net.chrisparton.sparkled.model.animation.AnimationEffect;
import net.chrisparton.sparkled.model.render.RenderedChannel;
import net.chrisparton.sparkled.model.render.RenderedFrame;
import net.chrisparton.sparkled.renderer.converter.ColourParamConverter;
import net.chrisparton.sparkled.renderer.converter.LengthParamConverter;

import java.awt.*;

public class LineEffectRenderer extends EffectRenderer {

    private ColourParamConverter colourParamConverter = new ColourParamConverter();
    private LengthParamConverter lengthParamConverter = new LengthParamConverter();

    @Override
    public void render(RenderedChannel channel, RenderedFrame frame, AnimationEffect effect, double progress) {
        int startLed = 0;
        int endLed = channel.getLedCount() - 1;
        int ledCount = endLed - startLed + 1;

        Color color = colourParamConverter.convert(effect);
        int length = lengthParamConverter.convert(effect);

        int firstLitLed = startLed + (int) Math.round((ledCount + length) * progress) - length;
        int lastLitLed = firstLitLed + length - 1;

        if (firstLitLed <= endLed) {
            for (int i = constrain(startLed, endLed, firstLitLed); i <= constrain(startLed, endLed - 1, lastLitLed); i++) {
                frame.getLed(i).addRgb(color);
            }
        }
    }

    private int constrain(int min, int max, int value) {
        return Math.max(min, Math.min(max, value));
    }
}
