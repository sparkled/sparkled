package io.sparkled.model.render;

import java.util.ArrayList;
import java.util.List;

public class RenderedStagePropData {

    public static final RenderedStagePropData EMPTY = new RenderedStagePropData();

    private final int ledCount;
    private final byte[] data;

    // Don't serialise frames to JSON, as each frame contains a reference to the (very large) data array
    private transient final List<RenderedFrame> frames;

    /**
     * Default constructor required for Gson.
     */
    @SuppressWarnings("unused")
    public RenderedStagePropData() {
        this(0, 0, 0, new byte[]{});
    }

    public RenderedStagePropData(int startFrame, int endFrame, int ledCount, byte[] data) {
        this.ledCount = ledCount;
        this.data = data;

        int frameCount = endFrame - startFrame + 1;
        this.frames = new ArrayList<>(frameCount);

        for (int frameIndex = startFrame; frameIndex <= endFrame; ++frameIndex) {
            this.frames.add(new RenderedFrame(startFrame, frameIndex, ledCount, data));
        }
    }

    public byte[] getData() {
        return data;
    }

    public List<RenderedFrame> getFrames() {
        return frames;
    }

    public int getLedCount() {
        return ledCount;
    }
}
