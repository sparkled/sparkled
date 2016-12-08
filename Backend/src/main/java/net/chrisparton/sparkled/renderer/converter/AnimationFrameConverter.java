package net.chrisparton.sparkled.renderer.converter;

import net.chrisparton.sparkled.renderer.data.RenderedFrame;
import net.chrisparton.sparkled.renderer.data.Led;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class AnimationFrameConverter {

    private List<RenderedFrame> frames;

    public AnimationFrameConverter(List<RenderedFrame> frames) {
        this.frames = frames;
    }

    public ByteArrayOutputStream convert() throws IOException {
        int frameCount = frames.size();
        int ledCount = frames.get(0).getLeds().size();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(frameCount * ledCount * ConverterConstants.BYTES_PER_LED);

        for (RenderedFrame frame : frames) {
            generateFrameData(frame, baos);
        }

        return baos;
    }

    private void generateFrameData(RenderedFrame frame, ByteArrayOutputStream baos) throws IOException {
        List<Led> leds = frame.getLeds();

        for (Led led : leds) {
            byte[] ledData = LedDataConverter.generate16BitLedData(led);
            baos.write(ledData);
        }
    }
}
