package net.chrisparton.sparkled.renderer.effect;

import net.chrisparton.sparkled.converter.ColourParamConverter;
import net.chrisparton.sparkled.entity.AnimationEffect;
import net.chrisparton.sparkled.renderdata.RenderedChannel;
import net.chrisparton.sparkled.renderdata.RenderedFrame;

import java.awt.*;

public class FillEffectRenderer extends EffectRenderer {

    @Override
    public void render(RenderedChannel channel, RenderedFrame frame, AnimationEffect effect, int progressPercentage) {
        Color color = new ColourParamConverter().convert(effect);

        for (int i = 0; i < frame.getLedCount(); i++) {
            frame.getLed(i).addRgb(color);
        }
    }
}
