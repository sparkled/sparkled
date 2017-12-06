package net.chrisparton.sparkled.renderer.easing.function;

import net.chrisparton.sparkled.renderer.easing.EasingFunction;

public class ConstantMidpointEasing implements EasingFunction {
    @Override
    public float getProgress(float currentFrame, float durationFrames) {
        return .5f;
    }
}
