package net.chrisparton.sparkled.renderer.effect;

import net.chrisparton.sparkled.model.animation.AnimationEasing;
import net.chrisparton.sparkled.model.animation.AnimationEasingTypeCode;
import net.chrisparton.sparkled.model.animation.AnimationEffect;
import net.chrisparton.sparkled.model.render.RenderedChannel;
import net.chrisparton.sparkled.model.render.RenderedFrame;

public abstract class EffectRenderer {

    public final void render(RenderedChannel channel, RenderedFrame frame, AnimationEffect effect) {
        double progress = getProgress(frame, effect);
        render(channel, frame, effect, progress);
    }

    private double getProgress(RenderedFrame frame, AnimationEffect effect) {
        AnimationEasingTypeCode easingTypeCode = AnimationEasingTypeCode.valueOf(effect.getEasingType().name());
        AnimationEasing easing = easingTypeCode.getEasingType().getEasing();

        int startFrame = effect.getStartFrame();
        int duration = effect.getEndFrame() - startFrame;

        int repetitions = effect.getRepetitions();
        int framesPerRepetition = duration / repetitions;
        int repetitionFrameNumber = (frame.getFrameNumber() - startFrame) % (framesPerRepetition  + 1);

        double progress = easing.getProgress(repetitionFrameNumber, 0, 1, framesPerRepetition);
        if (progress < 0 || progress > 1) {
            throw new IllegalStateException("Animation progress is out of bounds: " + progress);
        }

        return effect.isReverse() ? 1 - progress : progress;
    }

    protected abstract void render(RenderedChannel channel, RenderedFrame frame, AnimationEffect effect, double progress);
}