package net.chrisparton.sparkled.renderer.effect;

import net.chrisparton.sparkled.entity.AnimationEasing;
import net.chrisparton.sparkled.entity.AnimationEasingTypeCode;
import net.chrisparton.sparkled.entity.AnimationEffect;
import net.chrisparton.sparkled.entity.AnimationEffectChannel;
import net.chrisparton.sparkled.renderer.data.AnimationFrame;

public abstract class EffectRenderer {

    public final void render(AnimationEffectChannel channel, AnimationFrame frame, AnimationEffect effect) {
        int progress = getProgress(frame, effect);
        render(channel, frame, effect, progress);
    }

    private int getProgress(AnimationFrame frame, AnimationEffect effect) {
        AnimationEasingTypeCode easingTypeCode = AnimationEasingTypeCode.valueOf(effect.getEasingType().name());
        AnimationEasing easing = easingTypeCode.getEasingType().getEasing();

        int startFrame = effect.getStartFrame();
        int duration = effect.getEndFrame() - startFrame;

        int repetitions = effect.getRepetitions();
        int framesPerRepetition = duration / repetitions;
        int repetitionFrameNumber = (frame.getFrameNumber() - startFrame) % framesPerRepetition;

        double progress = easing.getProgress(repetitionFrameNumber, 0, 1, framesPerRepetition);
        if (progress < 0 || progress > 1) {
            throw new IllegalStateException("Animation progress is out of bounds.");
        }
        return (int) Math.round(100 * progress);
    }

    public abstract void render(AnimationEffectChannel channel, AnimationFrame frame, AnimationEffect effect, int progressPercentage);
}