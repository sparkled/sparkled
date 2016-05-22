package net.chrisparton.sparkled.renderer.converter;

import com.google.gson.Gson;
import net.chrisparton.sparkled.entity.*;
import net.chrisparton.sparkled.renderer.Renderer;
import net.chrisparton.sparkled.renderer.data.AnimationFrame;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AnimationFrameConverterTest {

    private Gson gson = new Gson();

    @Test
    public void can_render() throws IOException {
        SongAnimationData animationData = new SongAnimationData();

        {
            AnimationEffectChannel channel = createAnimationEffectChannel(0, 9);
            AnimationEffect flashEffect = createAnimationEffect(AnimationEffectTypeCode.FLASH, 0, 29);
            flashEffect.getParams().add(createAnimationEffectParam(AnimationEffectTypeParamCode.COLOUR, "#ff00ff"));
            channel.getEffects().add(flashEffect);
            animationData.getChannels().add(channel);
        }
        {
            AnimationEffectChannel channel = createAnimationEffectChannel(10, 19);
            AnimationEffect lineRightEffect = createAnimationEffect(AnimationEffectTypeCode.LINE_RIGHT, 30, 59);
            lineRightEffect.getParams().add(createAnimationEffectParam(AnimationEffectTypeParamCode.COLOUR, "#00ff00"));
            channel.getEffects().add(lineRightEffect);
            animationData.getChannels().add(channel);
        }

        Song song = new Song();
        song.setDurationSeconds(1);
        song.setAnimationData(gson.toJson(animationData));

        List<AnimationFrame> renderedFrames = new Renderer(song, 0, song.getDurationSeconds()).render();

        AnimationFrameConverter animationFrameConverter = new AnimationFrameConverter(renderedFrames);
        ByteArrayOutputStream convert = animationFrameConverter.convert();
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