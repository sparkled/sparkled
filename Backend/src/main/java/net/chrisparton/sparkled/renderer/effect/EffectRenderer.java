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
        double progress = easing.getProgress(frame.getFrameNumber() - startFrame, 0, 1, duration);
        return (int) Math.round(100 * progress);
    }

    public abstract void render(AnimationEffectChannel channel, AnimationFrame frame, AnimationEffect effect, int progressPercentage);
}
