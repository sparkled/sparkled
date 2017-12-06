package net.chrisparton.sparkled.util;

import com.google.gson.Gson;
import net.chrisparton.sparkled.model.animation.SongAnimationData;
import net.chrisparton.sparkled.model.animation.effect.Effect;
import net.chrisparton.sparkled.model.animation.effect.EffectChannel;
import net.chrisparton.sparkled.model.entity.Song;
import net.chrisparton.sparkled.model.entity.SongAnimation;
import net.chrisparton.sparkled.model.render.RenderedChannel;
import net.chrisparton.sparkled.model.render.RenderedChannelMap;
import net.chrisparton.sparkled.renderer.Renderer;

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
