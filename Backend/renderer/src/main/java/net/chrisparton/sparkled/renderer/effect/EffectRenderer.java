package net.chrisparton.sparkled.renderer.effect;

import net.chrisparton.sparkled.model.animation.effect.Effect;
import net.chrisparton.sparkled.model.render.RenderedChannel;
import net.chrisparton.sparkled.model.render.RenderedFrame;
import net.chrisparton.sparkled.renderer.context.RenderContext;
import net.chrisparton.sparkled.renderer.easing.EasingFunction;
import net.chrisparton.sparkled.renderer.easing.EasingFunctions;

public abstract class EffectRenderer {

    public final void render(RenderedChannel channel, RenderedFrame frame, Effect effect) {
        float progress = getProgress(frame, effect);
        RenderContext ctx = new RenderContext(channel, frame, effect, progress);
        render(ctx);
    }

    private float getProgress(RenderedFrame frame, Effect effect) {
        EasingFunction easingFunction = EasingFunctions.get(effect.getEasing().getType());

        int startFrame = effect.getStartFrame();
        int duration = effect.getEndFrame() - startFrame + 1;

        int repetitions = effect.getRepetitions();
        int framesPerRepetition = duration / repetitions;
        int repetitionFrameNumber = (frame.getFrameNumber() - startFrame) % (framesPerRepetition + 1);


        float progress = easingFunction.getProgress(effect.getEasing(), repetitionFrameNumber, framesPerRepetition);
        if (progress < 0 || progress > 1) {
            throw new IllegalStateException("Animation progress is out of bounds: " + progress);
        }

        return effect.isReverse() ? 1 - progress : progress;
    }

    protected abstract void render(RenderContext ctx);
}