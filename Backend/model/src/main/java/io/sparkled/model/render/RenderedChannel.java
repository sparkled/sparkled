package io.sparkled.model.render;

import java.util.ArrayList;
import java.util.List;

public class RenderedChannel {

    private final List<RenderedFrame> frames;
    private final int ledCount;
    private final int framesPerSecond;

    /**
     * Default constructor required for Gson.
     */
    @SuppressWarnings("unused")
    public RenderedChannel() {
        this(0, 0, 0, 0);
    }

    public RenderedChannel(int startFrame, int endFrame, int ledCount, int framesPerSecond) {
        this.frames = new ArrayList<>(endFrame - startFrame);
        this.ledCount = ledCount;
        this.framesPerSecond = framesPerSecond;

        for (int frame = startFrame; frame <= endFrame; ++frame) {
            this.frames.add(new RenderedFrame(frame, ledCount));
        }
    }

    public List<RenderedFrame> getFrames() {
        return frames;
    }

    public int getLedCount() {
        return ledCount;
    }

    public int getFramesPerSecond() {
        return framesPerSecond;
    }
}
