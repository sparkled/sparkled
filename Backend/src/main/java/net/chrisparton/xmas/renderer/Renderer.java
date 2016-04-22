package net.chrisparton.xmas.renderer;

import com.google.gson.Gson;
import net.chrisparton.xmas.entity.*;
import net.chrisparton.xmas.renderer.data.AnimationFrame;
import net.chrisparton.xmas.renderer.effect.EffectRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Renderer {

    private static final Logger logger = Logger.getLogger(Renderer.class.getName());

    private static final int FRAMES_PER_SECOND = 60;
    private final List<AnimationFrame> frameList;
    private final Gson gson = new Gson();

    private Song song;
    private SongAnimationData animationData;

    public Renderer(Song song) {
        this.song = song;
        this.animationData = gson.fromJson(song.getAnimationData(), SongAnimationData.class);
        this.frameList = createFrameList();
    }

    private List<AnimationFrame> createFrameList() {
        int frameCount = song.getDurationSeconds() * FRAMES_PER_SECOND;
        List<AnimationFrame> renderedFrames = new ArrayList<>(frameCount);

        int ledCount = getLedCount(animationData.getChannels());
        for (int frameNumber = 1; frameNumber <= frameCount; frameNumber++) {
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

        for (int frameNumber = effect.getStartFrame(); frameNumber <= effect.getEndFrame(); frameNumber++) {
            AnimationFrame frame = frameList.get(frameNumber - 1);
            renderer.render(channel, frame, effect);
        }
    }
}
