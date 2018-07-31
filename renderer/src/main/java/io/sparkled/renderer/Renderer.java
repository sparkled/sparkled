package io.sparkled.renderer;

import com.google.gson.Gson;
import io.sparkled.model.animation.SongAnimationData;
import io.sparkled.model.animation.effect.Effect;
import io.sparkled.model.animation.effect.EffectChannel;
import io.sparkled.model.animation.effect.EffectTypeCode;
import io.sparkled.model.entity.Song;
import io.sparkled.model.entity.SongAnimation;
import io.sparkled.model.render.Led;
import io.sparkled.model.render.RenderedStagePropData;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.model.render.RenderedFrame;
import io.sparkled.renderer.effect.EffectRenderer;
import io.sparkled.renderer.effect.FlashEffectRenderer;
import io.sparkled.renderer.effect.LineEffectRenderer;
import io.sparkled.renderer.effect.SplitLineEffectRenderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Renderer {

    private static Map<EffectTypeCode, EffectRenderer> effectTypeRenderers = new HashMap<>();

    static {
        effectTypeRenderers.put(EffectTypeCode.FLASH, new FlashEffectRenderer());
        effectTypeRenderers.put(EffectTypeCode.LINE, new LineEffectRenderer());
        effectTypeRenderers.put(EffectTypeCode.SPLIT_LINE, new SplitLineEffectRenderer());
    }

    private Song song;
    private SongAnimationData animationData;
    private int startFrame;
    private int durationFrames;

    public Renderer(Song song, SongAnimation songAnimation) {
        this(song, songAnimation, 0, song.getDurationFrames());
    }

    public Renderer(Song song, SongAnimation songAnimation, int startFrame, int durationFrames) {
        this.song = song;
        this.animationData = new Gson().fromJson(songAnimation.getAnimationData(), SongAnimationData.class);
        this.startFrame = startFrame;
        this.durationFrames = durationFrames;
    }

    public RenderedStagePropDataMap render() {
        List<EffectChannel> channels = animationData.getChannels();
        RenderedStagePropDataMap renderedChannels = new RenderedStagePropDataMap();

        channels.forEach(channel ->
                renderedChannels.put(channel.getPropCode(), renderChannel(channel))
        );
        return renderedChannels;
    }

    private RenderedStagePropData renderChannel(EffectChannel channel) {
        final int endFrame = Math.min(song.getDurationFrames() - 1, startFrame + durationFrames - 1);

        int frameCount = endFrame - startFrame + 1;
        byte[] data = new byte[frameCount * channel.getLedCount() * Led.BYTES_PER_LED];
        RenderedStagePropData renderedStagePropData = new RenderedStagePropData(startFrame, endFrame, channel.getLedCount(), data);
        channel.getEffects().forEach(effect -> renderEffect(song, renderedStagePropData, effect));

        return renderedStagePropData;
    }

    private void renderEffect(Song song, RenderedStagePropData renderedStagePropData, Effect effect) {
        EffectTypeCode effectTypeCode = effect.getType();
        EffectRenderer renderer = effectTypeRenderers.get(effectTypeCode);

        int startFrame = Math.max(this.startFrame, effect.getStartFrame());
        int endFrame = Math.min(this.startFrame + (this.durationFrames - 1), effect.getEndFrame());

        for (int frameNumber = startFrame; frameNumber <= endFrame; frameNumber++) {
            RenderedFrame frame = renderedStagePropData.getFrames().get(frameNumber - this.startFrame);
            renderer.render(song, renderedStagePropData, frame, effect);
        }
    }
}
