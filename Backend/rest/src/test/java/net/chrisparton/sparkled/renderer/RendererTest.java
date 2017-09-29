package net.chrisparton.sparkled.renderer;

import com.google.gson.Gson;
import net.chrisparton.sparkled.entity.*;
import net.chrisparton.sparkled.renderdata.Led;
import net.chrisparton.sparkled.renderdata.RenderedChannel;
import net.chrisparton.sparkled.renderdata.RenderedChannelMap;
import net.chrisparton.sparkled.renderdata.RenderedFrame;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;

public class RendererTest {

    private static final String CHANNEL_CODE = "CH1";

    private Gson gson = new Gson();

    @Test
    public void can_render() {
        final int ledCount = 10;
        final int songFrames = 60;
        final int effectStartFrame = 20;
        final int effectEndFrame = 50;
        final int renderStartFrame = effectStartFrame - 5;
        final int renderDuration = (effectEndFrame + 5) - renderStartFrame;

        SongAnimationData animationData = new SongAnimationData();

        AnimationEffectChannel channel = createAnimationEffectChannel(CHANNEL_CODE, 0, ledCount - 1);
        AnimationEffect flashEffect = createAnimationEffect(AnimationEffectTypeCode.FILL, effectStartFrame, effectEndFrame);
        flashEffect.getParams().add(createAnimationEffectParam(AnimationEffectTypeParamCode.COLOUR, "#ffffff"));
        channel.getEffects().add(flashEffect);
        animationData.getChannels().add(channel);

        final int fps = 60;
        Song song = new Song();
        song.setFramesPerSecond(fps);
        song.setDurationFrames(songFrames);
        song.setAnimationData(gson.toJson(animationData));

        RenderedChannelMap renderedChannels = new Renderer(song, renderStartFrame, renderDuration).render();
        assertThat(renderedChannels.size(), is(1));

        RenderedChannel renderedChannel = renderedChannels.get(CHANNEL_CODE);
        assertNotNull(renderedChannel);

        assertThat(renderedChannel.getFrames().size(), is(renderDuration));
        assertThat(renderedChannel.getLedCount(), is(ledCount));

        for (int i = 0; i < renderDuration; i++) {
            RenderedFrame renderedFrame = renderedChannel.getFrames().get(i);
            int frameNumber = renderedFrame.getFrameNumber();
            assertThat(frameNumber, is(i + renderStartFrame));
            assertThat(renderedFrame.getLedCount(), is(ledCount));

            int[] expectedLed = new int[] {0, 0, 0};

            if (frameNumber >= effectStartFrame && frameNumber <= effectEndFrame) {
                expectedLed = new int[] {255, 255, 255};
            }

            for (int j = 0; j < ledCount; j++) {
                Led renderedLed = renderedFrame.getLed(j);
                assertThat(renderedLed.getR(), is(expectedLed[0]));
                assertThat(renderedLed.getG(), is(expectedLed[1]));
                assertThat(renderedLed.getB(), is(expectedLed[2]));
            }
        }
    }

    private AnimationEffectChannel createAnimationEffectChannel(String code, int startLed, int endLed) {
        AnimationEffectChannel channel = new AnimationEffectChannel();
        channel.setCode(code);
        channel.setStartLed(startLed);
        channel.setEndLed(endLed);

        return channel;
    }

    private AnimationEffect createAnimationEffect(AnimationEffectTypeCode effectTypeCode, int startFrame, int endFrame) {
        AnimationEffect effect = new AnimationEffect();
        effect.setEffectType(effectTypeCode);
        effect.setEasingType(AnimationEasingTypeCode.LINEAR);
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
