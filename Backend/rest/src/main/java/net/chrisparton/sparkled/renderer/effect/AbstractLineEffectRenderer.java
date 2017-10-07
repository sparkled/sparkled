package net.chrisparton.sparkled.renderer.effect;

import net.chrisparton.sparkled.converter.ColourParamConverter;
import net.chrisparton.sparkled.converter.LengthParamConverter;
import net.chrisparton.sparkled.entity.AnimationEffect;
import net.chrisparton.sparkled.renderdata.RenderedChannel;
import net.chrisparton.sparkled.renderdata.RenderedFrame;

import java.awt.*;

public abstract class AbstractLineEffectRenderer extends EffectRenderer {

    private ColourParamConverter colourParamConverter = new ColourParamConverter();
    private LengthParamConverter lengthParamConverter = new LengthParamConverter();

    @Override
    public void render(RenderedChannel channel, RenderedFrame frame, AnimationEffect effect, int progressPercentage) {
        int startLed = 0;
        int endLed = channel.getLedCount();
        int ledCount = endLed - startLed;

        Color color = colourParamConverter.convert(effect);
        int length = lengthParamConverter.convert(effect);

        int adjustedProgressPercentage = calculateProgressPercentage(progressPercentage);
        int firstLitLed = startLed + (int) Math.round((ledCount + length) * adjustedProgressPercentage / 100.0) - length;
        int lastLitLed = firstLitLed + length - 1;

        if (firstLitLed <= endLed) {
            for (int i = constrain(startLed, endLed, firstLitLed); i <= constrain(startLed, endLed - 1, lastLitLed); i++) {
                frame.getLed(i).addRgb(
                        color.getRed(),
                        color.getGreen(),
                        color.getBlue()
                );
            }
        }
    }

    private int constrain(int min, int max, int value) {
        return Math.max(min, Math.min(max, value));
    }

    protected abstract int calculateProgressPercentage(int progressPercentage);
}
