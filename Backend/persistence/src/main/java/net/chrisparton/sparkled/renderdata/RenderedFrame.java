package net.chrisparton.sparkled.renderdata;

import java.util.ArrayList;
import java.util.List;

public class RenderedFrame {

    private int frameNumber;
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

    public byte[] getLedData() {
        return ledData;
    }

    public int getLedCount() {
        return ledData.length / 3;
    }

    public Led getLed(int ledIndex) {
        return new Led(ledData, ledIndex);
    }

    public List<Led> getLeds() {
        int ledCount = getLedCount();
        List<Led> leds = new ArrayList<>(ledCount);
        for (int i = 0; i < ledCount; i++) {
            leds.add(getLed(i));
        }

        return leds;
    }
}
