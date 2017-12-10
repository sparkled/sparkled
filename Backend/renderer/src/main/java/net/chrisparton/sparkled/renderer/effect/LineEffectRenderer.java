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
        renderLine(ctx, startLed, endLed, false);
    }

    void renderLine(RenderContext ctx, int startLed, int endLed, boolean mirror) {
        int ledCount = endLed - startLed + 1;
        int length = ParamUtils.getIntegerValue(ctx.getEffect(), ParamName.LENGTH);

        float progress = mirror ? 1 - ctx.getProgress() : ctx.getProgress();
        int firstLitLed = startLed + Math.round((ledCount + length) * progress) - length;
        int lastLitLed = firstLitLed + length - 1;

        if (firstLitLed <= endLed && lastLitLed >= 0) {
            for (int i = constrain(startLed, endLed, firstLitLed); i <= constrain(startLed, endLed, lastLitLed); i++) {
                FillUtils.fill(ctx, ctx.getFrame().getLed(i), 1f);

                if (mirror) {
                    FillUtils.fill(ctx, ctx.getFrame().getLed((endLed * 2) + (1 - endLed % 2) - i), 1f);
                }
            }
        }
    }

    private int constrain(int min, int max, int value) {
        return Math.max(min, Math.min(max, value));
    }
}
