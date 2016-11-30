package net.chrisparton.sparkled.renderer;

import com.google.gson.Gson;
import net.chrisparton.sparkled.entity.*;
import net.chrisparton.sparkled.renderer.data.AnimationFrame;
import net.chrisparton.sparkled.renderer.effect.EffectRenderer;

import java.util.ArrayList;
import java.util.List;

public class Renderer {

    private final List<AnimationFrame> frameList;
    private final Gson gson = new Gson();

    private Song song;
    private int startFrame;
    private int durationFrames;
    private SongAnimationData animationData;

    public Renderer(Song song, int startFrame, int durationFrames) {
        this.song = song;
        this.startFrame = startFrame;
        this.durationFrames = durationFrames;
        this.animationData = gson.fromJson(song.getAnimationData(), SongAnimationData.class);
        this.frameList = createFrameList();
    }

    private List<AnimationFrame> createFrameList() {
        int songFrameCount = song.getDurationFrames();

        int endFrame = startFrame + durationFrames;
        if (endFrame > songFrameCount) {
            endFrame = songFrameCount;
        }

        int frameCount = endFrame - startFrame;
        List<AnimationFrame> renderedFrames = new ArrayList<>(frameCount);

        int ledCount = getLedCount(animationData.getChannels()) + 1;
        for (int frameNumber = startFrame; frameNumber < startFrame + frameCount; frameNumber++) {
            renderedFrames.add(new AnimationFrame(frameNumber, ledCount));
        }

        return renderedFrames;
    }

    private int getLedCount(List<AnimationEffectChannel> channels) {
        return channels
                .stream()
                .mapToInt(AnimationEffectChannel::getEndLed)
                .max().orElse(0);
    }

    public List<AnimationFrame> render() {
        List<AnimationEffectChannel> channels = animationData.getChannels();
        channels.forEach(this::renderChannel);

        return frameList;
    }

    private void renderChannel(AnimationEffectChannel channel) {
        channel.getEffects().forEach(effect -> renderEffect(channel, effect));
    }

    private void renderEffect(AnimationEffectChannel channel, AnimationEffect effect) {
        AnimationEffectTypeCode effectTypeCode = effect.getEffectType();
        EffectRenderer renderer = effectTypeCode.getRenderer();

        int startFrame = Math.max(this.startFrame, effect.getStartFrame());
        int endFrame = Math.min(this.startFrame + frameList.size(), effect.getEndFrame());
        for (int frameNumber = startFrame; frameNumber < endFrame; frameNumber++) {
            AnimationFrame frame = frameList.get(frameNumber - this.startFrame);
            renderer.render(channel, frame, effect);
        }
    }
}
