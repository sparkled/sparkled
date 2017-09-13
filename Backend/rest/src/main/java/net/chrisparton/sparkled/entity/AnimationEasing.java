package net.chrisparton.sparkled.entity;

public interface AnimationEasing {

    /**
     * @param currentFrame The current animation effect frame being rendered
     * @param startFrame The beginning frame of the animation effect
     * @param changeValue The change between the beginning and destination frames
     * @param durationFrames The number of frames in the animation effect
     * @return A value between 0 and 1 describing the overall progress of the animation effect
     */
    double getProgress(float currentFrame, float startFrame, float changeValue, float durationFrames);
}
