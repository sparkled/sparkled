package io.sparkled.renderer.context;

import io.sparkled.model.animation.effect.Effect;
import io.sparkled.model.entity.Sequence;
import io.sparkled.model.render.RenderedFrame;
import io.sparkled.model.render.RenderedStagePropData;

public class RenderContext {
    private final Sequence sequence;
    private final RenderedStagePropData channel;
    private final RenderedFrame frame;
    private final Effect effect;
    private final float progress;

    public RenderContext(Sequence sequence, RenderedStagePropData channel, RenderedFrame frame, Effect effect, float progress) {
        this.sequence = sequence;
        this.channel = channel;
        this.frame = frame;
        this.effect = effect;
        this.progress = progress;
    }

    public Sequence getSequence() {
        return sequence;
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
