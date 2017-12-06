package net.chrisparton.sparkled.renderer.fill.function;

import net.chrisparton.sparkled.model.render.Led;
import net.chrisparton.sparkled.model.render.RenderedFrame;
import net.chrisparton.sparkled.renderer.context.RenderContext;
import net.chrisparton.sparkled.renderer.fill.FillFunction;

import java.awt.*;

public class RainbowFill implements FillFunction {

    private static final float SATURATION = .95f;

    @Override
    public void fill(RenderContext ctx, Led led, float alpha) {
        RenderedFrame frame = ctx.getFrame();
        float ledPosition = (float) led.getLedNumber() / frame.getLedCount();
        float progress = (float) frame.getFrameNumber() / ctx.getChannel().getFramesPerSecond();
        float hue = ledPosition + progress % 1f;

        Color color = Color.getHSBColor(hue, SATURATION, alpha);
        led.addColor(color);
    }
}
