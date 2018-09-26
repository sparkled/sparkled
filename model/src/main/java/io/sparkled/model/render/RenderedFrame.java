package io.sparkled.model.render;

public class RenderedFrame {

    private final int startFrame;
    private final int frameNumber;
    private final int ledCount;
    private final byte[] data;

    /**
     * Default constructor required for Gson.
     */
    @SuppressWarnings("unused")
    public RenderedFrame() {
        this(0, 0, 0, new byte[]{});
    }

    public RenderedFrame(int startFrame, int frameNumber, int ledCount, byte[] data) {
        this.startFrame = startFrame;
        this.frameNumber = frameNumber;
        this.ledCount = ledCount;
        this.data = data;
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public byte[] getData() {
        int bytesPerFrame = ledCount * Led.BYTES_PER_LED;
        int offset = (frameNumber - startFrame) * bytesPerFrame;

        byte[] frameData = new byte[bytesPerFrame];
        System.arraycopy(data, offset, frameData, 0, bytesPerFrame);

        return frameData;
    }

    public int getLedCount() {
        return ledCount;
    }

    public Led getLed(int ledIndex) {
        int bytesPerFrame = ledCount * Led.BYTES_PER_LED;
        int offset = (frameNumber - startFrame) * bytesPerFrame;
        return new Led(data, ledIndex, offset);
    }
}
