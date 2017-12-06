package net.chrisparton.sparkled.renderer.effect;

import net.chrisparton.sparkled.model.animation.param.ParamName;
import net.chrisparton.sparkled.renderer.context.RenderContext;
import net.chrisparton.sparkled.renderer.util.FillUtils;
import net.chrisparton.sparkled.renderer.util.ParamUtils;

public class LineEffectRenderer extends EffectRenderer {

    @Override
    public void render(RenderContext ctx) {
        int startLed = 0;
        int endLed = ctx.getChannel().getLedCount() - 1;
        renderLine(ctx, startLed, endLed);
    }

    void renderLine(RenderContext ctx, int startLed, int endLed) {
        int ledCount = endLed - startLed + 1;
        int length = ParamUtils.getIntegerValue(ctx.getEffect(), ParamName.LENGTH);

        float progress = ctx.getProgress();
        int firstLitLed = startLed + Math.round((ledCount + length) * progress) - length;
        int lastLitLed = firstLitLed + length - 1;

        if (firstLitLed <= endLed && lastLitLed >= 0) {
            for (int i = constrain(startLed, endLed, firstLitLed); i <= constrain(startLed, endLed, lastLitLed); i++) {
                FillUtils.fill(ctx, ctx.getFrame().getLed(i), 1f);
            }
        }
    }

    private int constrain(int min, int max, int value) {
        return Math.max(min, Math.min(max, value));
    }
}
