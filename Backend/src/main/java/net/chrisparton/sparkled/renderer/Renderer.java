package net.chrisparton.sparkled.renderer;

import com.google.gson.Gson;
import net.chrisparton.sparkled.entity.*;
import net.chrisparton.sparkled.renderer.data.RenderedChannel;
import net.chrisparton.sparkled.renderer.data.RenderedFrame;
import net.chrisparton.sparkled.renderer.effect.EffectRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class Renderer {

    private static final Gson gson = new Gson();

    private Song song;
    private int startFrame;
    private int durationFrames;
    private SongAnimationData animationData;

    public Renderer(Song song, int startFrame, int durationFrames) {
        this.song = song;
        this.startFrame = startFrame;
        this.durationFrames = durationFrames;
        this.animationData = gson.fromJson(song.getAnimationData(), SongAnimationData.class);
    }

    public Map<String, RenderedChannel> render() {
        List<AnimationEffectChannel> channels = animationData.getChannels();
        Map<String, RenderedChannel> renderedChannels = new HashMap<>();

        channels.forEach(channel ->
            renderedChannels.put(channel.getCode(), renderChannel(channel))
        );
        return renderedChannels;
    }

    private RenderedChannel renderChannel(AnimationEffectChannel channel) {
        final int endFrame = Math.min(song.getDurationFrames(), startFrame + durationFrames - 1);
        RenderedChannel renderedChannel = new RenderedChannel(startFrame, endFrame, channel.getLedCount());
        channel.getEffects().forEach(effect -> renderEffect(renderedChannel, effect));

        return renderedChannel;
    }

    private void renderEffect(RenderedChannel renderedChannel, AnimationEffect effect) {
        AnimationEffectTypeCode effectTypeCode = effect.getEffectType();
        EffectRenderer renderer = effectTypeCode.getRenderer();

        int startFrame = Math.max(this.startFrame, effect.getStartFrame());
        int endFrame = Math.min(this.startFrame + this.durationFrames, effect.getEndFrame());

        for (int frameNumber = startFrame; frameNumber <= endFrame; frameNumber++) {
            RenderedFrame frame = renderedChannel.getFrames().get(frameNumber - this.startFrame);
            renderer.render(renderedChannel, frame, effect);
        }
    }
}
