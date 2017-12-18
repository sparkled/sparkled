package io.sparkled.renderer;

import com.google.gson.Gson;
import io.sparkled.model.animation.SongAnimationData;
import io.sparkled.model.animation.effect.Effect;
import io.sparkled.model.animation.effect.EffectChannel;
import io.sparkled.model.animation.effect.EffectTypeCode;
import io.sparkled.model.entity.Song;
import io.sparkled.model.entity.SongAnimation;
import io.sparkled.model.render.RenderedChannel;
import io.sparkled.model.render.RenderedChannelMap;
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

    public RenderedChannelMap render() {
        List<EffectChannel> channels = animationData.getChannels();
        RenderedChannelMap renderedChannels = new RenderedChannelMap();

        channels.forEach(channel ->
                renderedChannels.put(channel.getCode(), renderChannel(channel))
        );
        return renderedChannels;
    }

    private RenderedChannel renderChannel(EffectChannel channel) {
        final int endFrame = Math.min(song.getDurationFrames() - 1, startFrame + durationFrames - 1);
        RenderedChannel renderedChannel = new RenderedChannel(startFrame, endFrame, channel.getLedCount(), song.getFramesPerSecond());
        channel.getEffects().forEach(effect -> renderEffect(renderedChannel, effect));

        return renderedChannel;
    }

    private void renderEffect(RenderedChannel renderedChannel, Effect effect) {
        EffectTypeCode effectTypeCode = effect.getType();
        EffectRenderer renderer = effectTypeRenderers.get(effectTypeCode);

        int startFrame = Math.max(this.startFrame, effect.getStartFrame());
        int endFrame = Math.min(this.startFrame + (this.durationFrames - 1), effect.getEndFrame());

        for (int frameNumber = startFrame; frameNumber <= endFrame; frameNumber++) {
            RenderedFrame frame = renderedChannel.getFrames().get(frameNumber - this.startFrame);
            renderer.render(renderedChannel, frame, effect);
        }
    }
}
