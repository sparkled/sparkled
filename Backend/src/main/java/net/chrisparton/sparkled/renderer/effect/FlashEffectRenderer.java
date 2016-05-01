package net.chrisparton.sparkled.renderer.effect;

import net.chrisparton.sparkled.converter.ColourParamConverter;
import net.chrisparton.sparkled.entity.AnimationEffect;
import net.chrisparton.sparkled.entity.AnimationEffectChannel;
import net.chrisparton.sparkled.renderer.data.AnimationFrame;
import net.chrisparton.sparkled.renderer.util.ColorUtils;

import java.awt.*;

public class FlashEffectRenderer extends EffectRenderer {

    @Override
    public void render(AnimationEffectChannel channel, AnimationFrame frame, AnimationEffect effect) {
        int startLed = channel.getStartLed();
        int endLed = channel.getEndLed();
        int startFrame = effect.getStartFrame();
        int endFrame = effect.getEndFrame();
        int frameNumber = frame.getFrameNumber();

        Color color = new ColourParamConverter().convert(effect);

        double progress = (frameNumber - startFrame) / (double)(endFrame - startFrame) * Math.PI;
        double brightnessPercentage = 100 * Math.sin(progress);
        Color adjustedColor = ColorUtils.adjustBrightness(color, brightnessPercentage);

        frame.getLeds()
                .subList(startLed, endLed + 1)
                .forEach(led -> {
                    led.addRgb(
                            adjustedColor.getRed(),
                            adjustedColor.getGreen(),
                            adjustedColor.getBlue()
                    );
                });
    }
}
