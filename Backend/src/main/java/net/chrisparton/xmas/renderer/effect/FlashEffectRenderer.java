package net.chrisparton.xmas.renderer.effect;

import net.chrisparton.xmas.converter.ColourParamConverter;
import net.chrisparton.xmas.entity.*;
import net.chrisparton.xmas.renderer.data.AnimationFrame;
import net.chrisparton.xmas.renderer.util.ColorUtils;

import java.awt.*;

public class FlashEffectRenderer extends EffectRenderer {

    @Override
    public void render(AnimationEffectChannel channel, AnimationFrame frame, AnimationEffect effect) {
        int startLed = channel.getStartLed();
        int endLed = channel.getEndLed();
        int startFrame = effect.getStartFrame();
        int endFrame = effect.getEndFrame();
        int frameNumber = frame.getFrameNumber();

        AnimationEffectParam param = effect.getParams()
                .stream()
                .filter(p -> AnimationEffectTypeParamCode.COLOUR == p.getParamCode())
                .findFirst()
                .get();

        Color color = new ColourParamConverter().convert(param.getValue());

        double progress = (frameNumber - startFrame) / (double)(endFrame - startFrame) * Math.PI;
        double brightnessPercentage = 100 * Math.sin(progress);
        Color adjustedColor = ColorUtils.adjustBrightness(color, brightnessPercentage);

        frame.getLeds()
                .subList(startLed, endLed + 1)
                .forEach(led -> {
                    led.setR((short) adjustedColor.getRed());
                    led.setG((short) adjustedColor.getGreen());
                    led.setB((short) adjustedColor.getBlue());
                });
    }
}
