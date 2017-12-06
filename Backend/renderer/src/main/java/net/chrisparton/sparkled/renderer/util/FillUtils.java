package net.chrisparton.sparkled.renderer.util;

import net.chrisparton.sparkled.model.render.Led;
import net.chrisparton.sparkled.renderer.context.RenderContext;
import net.chrisparton.sparkled.renderer.fill.FillFunctions;

public class FillUtils {

    private FillUtils() {
    }

    public static void fill(RenderContext ctx, Led led, float alpha) {
        FillFunctions.get(ctx.getEffect().getFill().getType()).fill(ctx, led, alpha);
    }
}
