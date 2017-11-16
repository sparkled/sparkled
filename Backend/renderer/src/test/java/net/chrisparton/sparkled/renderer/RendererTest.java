package net.chrisparton.sparkled.renderer;

import com.google.gson.Gson;
import net.chrisparton.sparkled.model.animation.*;
import net.chrisparton.sparkled.model.entity.Song;
import net.chrisparton.sparkled.model.entity.SongAnimation;
import net.chrisparton.sparkled.model.render.RenderedChannel;
import net.chrisparton.sparkled.model.render.RenderedChannelMap;
import net.chrisparton.sparkled.model.render.RenderedFrame;
import org.junit.Test;

import java.util.List;

import static net.chrisparton.sparkled.util.matchers.SparkledMatchers.hasLeds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;

public class RendererTest {

    private static final String CHANNEL_CODE = "CH1";

    private Gson gson = new Gson();

    @Test
    public void can_render() {
        final int ledCount = 5;
        final int songFrames = 5;
        final int effectStartFrame = 1;
        final int effectEndFrame = 3;
        final int renderStartFrame = 0;
        final int renderDuration = 5;

        SongAnimationData animationData = new SongAnimationData();

        AnimationEffectChannel channel = createAnimationEffectChannel(CHANNEL_CODE, ledCount);
        AnimationEffect flashEffect = createAnimationEffect(AnimationEffectTypeCode.FLASH, effectStartFrame, effectEndFrame, AnimationEasingTypeCode.CONSTANT_MIDPOINT);
        flashEffect.getParams().add(createAnimationEffectParam(AnimationEffectTypeParamCode.COLOUR, "#ffffff"));
        channel.getEffects().add(flashEffect);
        animationData.getChannels().add(channel);

        Song song = new Song().setDurationFrames(songFrames);
        SongAnimation songAnimation = new SongAnimation().setAnimationData(gson.toJson(animationData));

        RenderedChannelMap renderedChannels = new Renderer(song, songAnimation, renderStartFrame, renderDuration).render();
        assertThat(renderedChannels.size(), is(1));

        RenderedChannel renderedChannel = renderedChannels.get(CHANNEL_CODE);
        assertNotNull(renderedChannel);
        List<RenderedFrame> frames = renderedChannel.getFrames();
        assertThat(frames.size(), is(renderDuration));
        assertThat(renderedChannel.getLedCount(), is(ledCount));

        final int b = 0x000000; // Black
        final int w = 0xFFFFFF; // White
        assertThat(renderedChannel, hasLeds(new int[][] {
                {b, b, b, b, b},
                {w, w, w, w, w},
                {w, w, w, w, w},
                {w, w, w, w, w},
                {b, b, b, b, b}
        }));
    }

    private AnimationEffectChannel createAnimationEffectChannel(String code, int leds) {
        AnimationEffectChannel channel = new AnimationEffectChannel();
        channel.setCode(code);
        channel.setStartLed(0);
        channel.setEndLed(leds - 1);

        return channel;
    }

    private AnimationEffect createAnimationEffect(AnimationEffectTypeCode effectTypeCode, int startFrame, int endFrame, AnimationEasingTypeCode easingType) {
        AnimationEffect effect = new AnimationEffect();
        effect.setEffectType(effectTypeCode);
        effect.setEasingType(easingType);
        effect.setStartFrame(startFrame);
        effect.setEndFrame(endFrame);

        return effect;
    }

    private AnimationEffectParam createAnimationEffectParam(AnimationEffectTypeParamCode typeParamCode, String value) {
        AnimationEffectParam effectParam = new AnimationEffectParam();
        effectParam.setParamCode(typeParamCode);
        effectParam.setValue(value);

        return effectParam;
    }
}
