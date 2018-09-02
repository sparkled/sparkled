package io.sparkled.renderer.fill.function;

import io.sparkled.model.animation.param.ParamName;
import io.sparkled.model.render.Led;
import io.sparkled.model.render.RenderedFrame;
import io.sparkled.renderer.context.RenderContext;
import io.sparkled.renderer.fill.FillFunction;
import io.sparkled.renderer.util.ParamUtils;

import java.awt.*;

public class RainbowFill implements FillFunction {

    private static final float SATURATION = .95f;

    @Override
    public void fill(RenderContext ctx, Led led, float alpha) {
        float cycleCount = ParamUtils.getDecimalValue(ctx.getEffect().getFill(), ParamName.CYCLE_COUNT);
        float cyclesPerSecond = ParamUtils.getDecimalValue(ctx.getEffect().getFill(), ParamName.CYCLES_PER_SECOND);

        RenderedFrame frame = ctx.getFrame();
        float ledPosition = (float) led.getLedNumber() / frame.getLedCount() * cycleCount;
        float progress = (float) frame.getFrameNumber() / ctx.getSequence().getFramesPerSecond() * cyclesPerSecond;
        float hue = ledPosition + progress % 1f;

        Color color = Color.getHSBColor(hue, SATURATION, alpha);
        led.addColor(color);
    }
}
