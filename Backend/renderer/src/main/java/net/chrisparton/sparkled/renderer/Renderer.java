package net.chrisparton.sparkled.renderer;

import com.google.gson.Gson;
import net.chrisparton.sparkled.model.animation.SongAnimationData;
import net.chrisparton.sparkled.model.animation.effect.Effect;
import net.chrisparton.sparkled.model.animation.effect.EffectChannel;
import net.chrisparton.sparkled.model.animation.effect.EffectTypeCode;
import net.chrisparton.sparkled.model.entity.Song;
import net.chrisparton.sparkled.model.entity.SongAnimation;
import net.chrisparton.sparkled.model.render.RenderedChannel;
import net.chrisparton.sparkled.model.render.RenderedChannelMap;
import net.chrisparton.sparkled.model.render.RenderedFrame;
import net.chrisparton.sparkled.renderer.effect.EffectRenderer;
import net.chrisparton.sparkled.renderer.effect.FlashEffectRenderer;
import net.chrisparton.sparkled.renderer.effect.LineEffectRenderer;
import net.chrisparton.sparkled.renderer.effect.SplitLineEffectRenderer;

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
