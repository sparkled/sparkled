package io.sparkled.renderer.easing.function;

import io.sparkled.model.animation.easing.Easing;
import io.sparkled.renderer.easing.EasingFunction;

/**
 * An easing function that interpolates uniformly between 0 and 1.
 */
public class LinearEasing implements EasingFunction {
    @Override
    public float getProgress(Easing easing, float currentFrame, float frameCount) {
        return currentFrame / (frameCount - 1);
    }
}
