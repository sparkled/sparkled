package net.chrisparton.sparkled.renderer.fill.function;

import net.chrisparton.sparkled.model.animation.param.ParamName;
import net.chrisparton.sparkled.model.render.Led;
import net.chrisparton.sparkled.renderer.context.RenderContext;
import net.chrisparton.sparkled.renderer.fill.FillFunction;
import net.chrisparton.sparkled.renderer.util.ColorUtils;
import net.chrisparton.sparkled.renderer.util.ParamUtils;

import java.awt.*;

public class SolidFill implements FillFunction {

    @Override
    public void fill(RenderContext ctx, Led led, float alpha) {
        Color color = ParamUtils.getColorValue(ctx.getEffect().getFill(), ParamName.COLOR);
        Color adjustedColor = ColorUtils.adjustBrightness(color, alpha);
        led.addColor(adjustedColor);
    }
}
