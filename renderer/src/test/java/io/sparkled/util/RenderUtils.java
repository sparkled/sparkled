package io.sparkled.util;

import com.google.gson.Gson;
import io.sparkled.model.animation.SequenceAnimationData;
import io.sparkled.model.animation.effect.Effect;
import io.sparkled.model.animation.effect.EffectChannel;
import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.SequenceAnimation;
import io.sparkled.model.render.RenderedStagePropData;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.renderer.Renderer;

import java.util.Collections;

public class RenderUtils {

    private static final String PROP_CODE = "A1";
    private static final Gson gson = new Gson();

    private RenderUtils() {
    }

    public static RenderedStagePropData render(Effect effect, int frameCount, int ledCount) {
        effect.setStartFrame(0);
        effect.setEndFrame(frameCount - 1);
        SequenceAnimationData animationData = new SequenceAnimationData();

        EffectChannel channel = new EffectChannel()
                .setEndLed(ledCount - 1)
                .setPropCode(PROP_CODE)
                .setEffects(Collections.singletonList(effect));

        animationData.getChannels().add(channel);

        Sequence sequence = new Sequence().setDurationFrames(frameCount);
        SequenceAnimation sequenceAnimation = new SequenceAnimation().setAnimationData(gson.toJson(animationData));

        RenderedStagePropDataMap renderedChannels = new Renderer(sequence, sequenceAnimation, 0, frameCount).render();
        return renderedChannels.get(PROP_CODE);
    }
}
