package io.sparkled.util;

import com.google.gson.Gson;
import io.sparkled.model.animation.SongAnimationData;
import io.sparkled.model.animation.effect.Effect;
import io.sparkled.model.animation.effect.EffectChannel;
import io.sparkled.model.entity.Song;
import io.sparkled.model.entity.SongAnimation;
import io.sparkled.model.render.RenderedChannel;
import io.sparkled.model.render.RenderedChannelMap;
import io.sparkled.renderer.Renderer;

import java.util.Collections;

public class RenderUtils {

    private static final String CHANNEL_CODE = "CH1";
    private static final Gson gson = new Gson();

    private RenderUtils() {
    }

    public static RenderedChannel render(Effect effect, int frameCount, int ledCount) {
        effect.setStartFrame(0);
        effect.setEndFrame(frameCount - 1);
        SongAnimationData animationData = new SongAnimationData();

        EffectChannel channel = new EffectChannel()
                .setEndLed(ledCount - 1)
                .setCode(CHANNEL_CODE)
                .setEffects(Collections.singletonList(effect));

        animationData.getChannels().add(channel);

        Song song = new Song().setDurationFrames(frameCount);
        SongAnimation songAnimation = new SongAnimation().setAnimationData(gson.toJson(animationData));

        RenderedChannelMap renderedChannels = new Renderer(song, songAnimation, 0, frameCount).render();
        return renderedChannels.get(CHANNEL_CODE);
    }
}
