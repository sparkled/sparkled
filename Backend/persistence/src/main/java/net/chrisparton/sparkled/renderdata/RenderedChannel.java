package net.chrisparton.sparkled.renderdata;

import java.util.ArrayList;
import java.util.List;

public class RenderedChannel {

    private final List<RenderedFrame> frames;
    private final int ledCount;

    /**
     * Default constructor required for Gson.
     */
    @SuppressWarnings("unused")
    public RenderedChannel() {
        this(0, 0, 0);
    }

    public RenderedChannel(int startFrame, int endFrame, int ledCount) {
        this.frames = new ArrayList<>(endFrame - startFrame);
        this.ledCount = ledCount;

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
}
