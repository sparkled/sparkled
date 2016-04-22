package net.chrisparton.xmas.renderer.data;

import java.util.ArrayList;
import java.util.List;

public class AnimationFrame {

    int frameNumber;
    private List<Led> leds;

    public AnimationFrame(int frameNumber, int ledCount) {
        this.frameNumber = frameNumber;
        this.leds = new ArrayList<>(ledCount);

        for (int i = 0; i < ledCount; i++) {
            leds.add(new Led());
        }
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public List<Led> getLeds() {
        return leds;
    }
}
