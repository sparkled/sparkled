package io.sparkled.renderer.fill;

import io.sparkled.model.animation.effect.Effect;
import io.sparkled.model.animation.fill.Fill;
import io.sparkled.model.render.Led;
import io.sparkled.renderer.context.RenderContext;

public interface FillFunction {

    /**
     * Fill the provided {@link Led} using the {@link Fill} configuration of the provided {@link Effect}.
     */
    void fill(RenderContext ctx, Led led, float alpha);
}
