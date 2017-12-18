package io.sparkled.renderer.easing;

import io.sparkled.model.animation.easing.Easing;

public interface EasingFunction {

    /**
     * @param easing         The easing configuration, including parameters
     * @param currentFrame   The current animation effect frame being rendered
     * @param durationFrames The number of frames in the animation effect (implementing functions will likely subtract 1
     *                       from this value, as currentFrame is zero-based)
     * @return A value between 0 and 1 describing the overall progress of the animation effect
     */
    float getProgress(Easing easing, float currentFrame, float durationFrames);
}
