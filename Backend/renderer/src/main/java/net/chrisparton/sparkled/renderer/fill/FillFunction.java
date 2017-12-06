package net.chrisparton.sparkled.renderer.fill;

import net.chrisparton.sparkled.model.animation.effect.Effect;
import net.chrisparton.sparkled.model.animation.fill.Fill;
import net.chrisparton.sparkled.model.render.Led;
import net.chrisparton.sparkled.renderer.context.RenderContext;

public interface FillFunction {

    /**
     * Fill the provided {@link Led} using the {@link Fill} configuration of the provided {@link Effect}.
     */
    void fill(RenderContext ctx, Led led, float alpha);
}
