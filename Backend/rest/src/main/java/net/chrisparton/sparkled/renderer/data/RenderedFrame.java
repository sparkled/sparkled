package net.chrisparton.sparkled.renderer.data;

public class RenderedFrame {

    int frameNumber;
    private final byte[] ledData;

    /**
     * Default constructor required for Gson.
     */
    @SuppressWarnings("unused")
    public RenderedFrame() {
        this(0, 0);
    }

    public RenderedFrame(int frameNumber, int ledCount) {
        this.frameNumber = frameNumber;
        this.ledData = new byte[ledCount * 3];
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public int getLedCount() {
        return ledData.length / 3;
    }

    public Led getLed(int ledIndex) {
        return new Led(ledData, ledIndex);
    }
}
