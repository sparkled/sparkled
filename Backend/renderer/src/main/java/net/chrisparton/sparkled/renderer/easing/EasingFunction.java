package net.chrisparton.sparkled.renderer.easing;

public interface EasingFunction {

    /**
     * @param currentFrame   The current animation effect frame being rendered
     * @param durationFrames The number of frames in the animation effect (implementing functions will likely subtract 1
     *                       from this value, as currentFrame is zero-based)
     * @return A value between 0 and 1 describing the overall progress of the animation effect
     */
    float getProgress(float currentFrame, float durationFrames);
}
