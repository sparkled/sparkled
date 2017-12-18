package io.sparkled.renderer.effect;

import io.sparkled.renderer.context.RenderContext;

public class SplitLineEffectRenderer extends EffectRenderer {

    @Override
    public void render(RenderContext ctx) {
        int start = 0;
        int end = ctx.getChannel().getLedCount() - 1;
        int middle = end / 2;

        LineEffectRenderer lineRenderer = new LineEffectRenderer();
        lineRenderer.renderLine(ctx, start, middle, true);
    }
}