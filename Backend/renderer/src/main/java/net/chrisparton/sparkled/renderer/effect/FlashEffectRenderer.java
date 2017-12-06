package net.chrisparton.sparkled.renderer.effect;

import net.chrisparton.sparkled.renderer.context.RenderContext;
import net.chrisparton.sparkled.renderer.util.FillUtils;

public class FlashEffectRenderer extends EffectRenderer {

    @Override
    public void render(RenderContext ctx) {
        float alpha = getAlpha(ctx.getProgress());

        for (int i = 0; i < ctx.getChannel().getLedCount(); i++) {
            FillUtils.fill(ctx, ctx.getFrame().getLed(i), alpha);
        }
    }

    /**
     * @param progress The 0 > 1 progress
     * @return The progress, transformed into 0 > 1 > 0
     */
    private float getAlpha(float progress) {
        float alpha = progress * 2;
        if (progress >= .5) {
            alpha = 1 - (progress - .5f) * 2;
        }

        // Remove floating point inaccuracies to ensure a symmetrical flash.
        return Math.round(alpha * 10000f) / 10000f;
    }
}
