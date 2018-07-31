package io.sparkled.renderer.context;

import io.sparkled.model.animation.effect.Effect;
import io.sparkled.model.entity.Song;
import io.sparkled.model.render.RenderedStagePropData;
import io.sparkled.model.render.RenderedFrame;

public class RenderContext {
    private final Song song;
    private final RenderedStagePropData channel;
    private final RenderedFrame frame;
    private final Effect effect;
    private final float progress;

    public RenderContext(Song song, RenderedStagePropData channel, RenderedFrame frame, Effect effect, float progress) {
        this.song = song;
        this.channel = channel;
        this.frame = frame;
        this.effect = effect;
        this.progress = progress;
    }

    public Song getSong() {
        return song;
    }

    public RenderedStagePropData getChannel() {
        return channel;
    }

    public RenderedFrame getFrame() {
        return frame;
    }

    public Effect getEffect() {
        return effect;
    }

    public float getProgress() {
        return progress;
    }
}
