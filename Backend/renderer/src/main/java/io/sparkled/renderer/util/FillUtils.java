package io.sparkled.renderer.util;

import io.sparkled.model.render.Led;
import io.sparkled.renderer.context.RenderContext;
import io.sparkled.renderer.fill.FillFunctions;

public class FillUtils {

    private FillUtils() {
    }

    public static void fill(RenderContext ctx, Led led, float alpha) {
        FillFunctions.get(ctx.getEffect().getFill().getType()).fill(ctx, led, alpha);
    }
}
