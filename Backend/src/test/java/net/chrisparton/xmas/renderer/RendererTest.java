package net.chrisparton.xmas.renderer;

import com.google.gson.Gson;
import net.chrisparton.xmas.entity.*;
import net.chrisparton.xmas.renderer.data.AnimationFrame;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class RendererTest {

    private Gson gson = new Gson();

    @Test
    public void can_render() {
        SongAnimationData animationData = new SongAnimationData();

        {
            AnimationEffectChannel channel = createAnimationEffectChannel(1, 10);
            AnimationEffect flashEffect = createAnimationEffect(AnimationEffectTypeCode.FLASH, 1, 30);
            flashEffect.getParams().add(createAnimationEffectParam(AnimationEffectTypeParamCode.COLOUR, "ff00ff"));
            channel.getEffects().add(flashEffect);
            animationData.getChannels().add(channel);
        }
        {
            AnimationEffectChannel channel = createAnimationEffectChannel(11, 20);
            AnimationEffect lineRightEffect = createAnimationEffect(AnimationEffectTypeCode.LINE_RIGHT, 31, 60);
            lineRightEffect.getParams().add(createAnimationEffectParam(AnimationEffectTypeParamCode.COLOUR, "00ff00"));
            channel.getEffects().add(lineRightEffect);
            animationData.getChannels().add(channel);
        }

        Song song = new Song();
        song.setDurationSeconds(1);
        song.setAnimationData(gson.toJson(animationData));

        List<AnimationFrame> renderedFrames = new Renderer(song).render();
        assertThat(renderedFrames.size(), is(60));
    }

    private AnimationEffectChannel createAnimationEffectChannel(int startLed, int endLed) {
        AnimationEffectChannel channel = new AnimationEffectChannel();
        channel.setStartLed(startLed);
        channel.setEndLed(endLed);

        return channel;
    }

    private AnimationEffect createAnimationEffect(AnimationEffectTypeCode effectTypeCode, int startFrame, int endFrame) {
        AnimationEffect effect = new AnimationEffect();
        effect.setEffectType(effectTypeCode);
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
