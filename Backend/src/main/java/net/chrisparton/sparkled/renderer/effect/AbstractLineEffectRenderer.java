package net.chrisparton.sparkled.renderer.effect;

import net.chrisparton.sparkled.converter.ColourParamConverter;
import net.chrisparton.sparkled.converter.LengthParamConverter;
import net.chrisparton.sparkled.entity.AnimationEffect;
import net.chrisparton.sparkled.entity.AnimationEffectChannel;
import net.chrisparton.sparkled.renderer.data.AnimationFrame;

import java.awt.*;

public abstract class AbstractLineEffectRenderer extends EffectRenderer {

    private ColourParamConverter colourParamConverter = new ColourParamConverter();
    private LengthParamConverter lengthParamConverter = new LengthParamConverter();

    @Override
    public void render(AnimationEffectChannel channel, AnimationFrame frame, AnimationEffect effect) {
        int startLed = channel.getStartLed();
        int endLed = channel.getEndLed();
        int ledCount = endLed - startLed;
        int startFrame = effect.getStartFrame();
        int endFrame = effect.getEndFrame();
        int frameNumber = frame.getFrameNumber();

        Color color = colourParamConverter.convert(effect);
        int length = lengthParamConverter.convert(effect);

        double progress = (frameNumber - startFrame) / (double) (endFrame - startFrame);
        double percentage = calculateProgressPercentage(progress);

        int firstLitLed = startLed + (int) Math.round((ledCount + length) * percentage / 100) - length;
        int lastLitLed = firstLitLed + length - 1;

        if (firstLitLed <= endLed) {
            frame.getLeds()
                    .subList(Math.max(startLed, firstLitLed), Math.min(lastLitLed, endLed) + 1)
                    .forEach(led -> {
                        led.addRgb(
                                color.getRed(),
                                color.getGreen(),
                                color.getBlue()
                        );
                    });
        }
    }

    protected abstract double calculateProgressPercentage(double progress);
}
