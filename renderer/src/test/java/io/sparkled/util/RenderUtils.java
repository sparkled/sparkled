package io.sparkled.util;

import io.sparkled.model.animation.SequenceChannelEffects;
import io.sparkled.model.animation.effect.Effect;
import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.SequenceChannel;
import io.sparkled.model.entity.StageProp;
import io.sparkled.model.render.RenderedStagePropData;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.model.util.GsonProvider;
import io.sparkled.renderer.Renderer;

import java.util.Collections;
import java.util.UUID;

public class RenderUtils {

    private static final UUID PROP_UUID = new UUID(0, 0);
    private static final String PROP_CODE = "TEST_PROP";

    private RenderUtils() {
    }

    public static RenderedStagePropData render(Effect effect, int frameCount, int ledCount) {
        effect.setStartFrame(0);
        effect.setEndFrame(frameCount - 1);

        SequenceChannelEffects animationData = new SequenceChannelEffects();
        animationData.getEffects().add(effect);

        Sequence sequence = new Sequence().setFramesPerSecond(60);
        String channelJson = GsonProvider.get().toJson(animationData);
        SequenceChannel sequenceChannel = new SequenceChannel().setStagePropUuid(PROP_UUID).setChannelJson(channelJson);

        StageProp stageProp = new StageProp().setCode(PROP_CODE).setUuid(PROP_UUID).setLedCount(ledCount);

        RenderedStagePropDataMap renderedChannels = new Renderer(
                sequence,
                Collections.singletonList(sequenceChannel),
                Collections.singletonList(stageProp),
                0,
                effect.getEndFrame()).render();
        return renderedChannels.get(PROP_UUID);
    }
}
