package net.chrisparton.sparkled.renderer;

import com.google.gson.Gson;
import net.chrisparton.sparkled.model.animation.AnimationEffect;
import net.chrisparton.sparkled.model.animation.AnimationEffectChannel;
import net.chrisparton.sparkled.model.animation.AnimationEffectTypeCode;
import net.chrisparton.sparkled.model.animation.SongAnimationData;
import net.chrisparton.sparkled.model.entity.Song;
import net.chrisparton.sparkled.model.entity.SongAnimation;
import net.chrisparton.sparkled.model.render.RenderedChannel;
import net.chrisparton.sparkled.model.render.RenderedChannelMap;
import net.chrisparton.sparkled.model.render.RenderedFrame;
import net.chrisparton.sparkled.renderer.effect.EffectRenderer;
import net.chrisparton.sparkled.renderer.effect.FlashEffectRenderer;
import net.chrisparton.sparkled.renderer.effect.LineEffectRenderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Renderer {

    private Song song;
    private SongAnimationData animationData;
    private int startFrame;
    private int durationFrames;

    private static Map<AnimationEffectTypeCode, EffectRenderer> effectTypeRenderers = new HashMap<>();

    static {
        effectTypeRenderers.put(AnimationEffectTypeCode.LINE, new LineEffectRenderer());
        effectTypeRenderers.put(AnimationEffectTypeCode.FLASH, new FlashEffectRenderer());
    }

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
        List<AnimationEffectChannel> channels = animationData.getChannels();
        RenderedChannelMap renderedChannels = new RenderedChannelMap();

        channels.forEach(channel ->
                renderedChannels.put(channel.getCode(), renderChannel(channel))
        );
        return renderedChannels;
    }

    private RenderedChannel renderChannel(AnimationEffectChannel channel) {
        final int endFrame = Math.min(song.getDurationFrames() - 1, startFrame + durationFrames - 1);
        RenderedChannel renderedChannel = new RenderedChannel(startFrame, endFrame, channel.getLedCount());
        channel.getEffects().forEach(effect -> renderEffect(renderedChannel, effect));

        return renderedChannel;
    }

    private void renderEffect(RenderedChannel renderedChannel, AnimationEffect effect) {
        AnimationEffectTypeCode effectTypeCode = effect.getEffectType();
        EffectRenderer renderer = effectTypeRenderers.get(effectTypeCode);

        int startFrame = Math.max(this.startFrame, effect.getStartFrame());
        int endFrame = Math.min(this.startFrame + (this.durationFrames - 1), effect.getEndFrame());

        for (int frameNumber = startFrame; frameNumber <= endFrame; frameNumber++) {
            RenderedFrame frame = renderedChannel.getFrames().get(frameNumber - this.startFrame);
            renderer.render(renderedChannel, frame, effect);
        }
    }
}
