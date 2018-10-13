package io.sparkled.renderer;

import io.sparkled.model.animation.ChannelPropPair;
import io.sparkled.model.animation.effect.Effect;
import io.sparkled.model.animation.effect.EffectTypeCode;
import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.SequenceChannel;
import io.sparkled.model.entity.StageProp;
import io.sparkled.model.render.Led;
import io.sparkled.model.render.RenderedFrame;
import io.sparkled.model.render.RenderedStagePropData;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.renderer.effect.EffectRenderer;
import io.sparkled.renderer.util.ChannelPropPairUtils;
import io.sparkled.renderer.util.EffectTypeRenderers;

import java.util.List;
import java.util.UUID;

public class Renderer {

    private final Sequence sequence;
    private final List<ChannelPropPair> channelPropPairs;
    private final int startFrame;
    private final int endFrame;

    public Renderer(Sequence sequence, List<SequenceChannel> sequenceChannels, List<StageProp> stageProps, int startFrame, int endFrame) {
        this.sequence = sequence;
        this.channelPropPairs = ChannelPropPairUtils.makePairs(sequenceChannels, stageProps);
        this.startFrame = startFrame;
        this.endFrame = endFrame;
    }

    public RenderedStagePropDataMap render() {
        RenderedStagePropDataMap renderedProps = new RenderedStagePropDataMap();

        channelPropPairs.forEach(cpp -> {
                    UUID stagePropUuid = cpp.getStageProp().getUuid();
                    RenderedStagePropData data = renderedProps.get(stagePropUuid);
                    renderedProps.put(stagePropUuid, renderChannel(cpp, data));
                }
        );
        return renderedProps;
    }

    private RenderedStagePropData renderChannel(ChannelPropPair channelPropPair, RenderedStagePropData renderedStagePropData) {
        if (renderedStagePropData == null) {
            int frameCount = endFrame - startFrame + 1;
            int leds = channelPropPair.getStageProp().getLedCount();
            byte[] data = new byte[frameCount * leds * Led.BYTES_PER_LED];
            renderedStagePropData = new RenderedStagePropData(startFrame, endFrame, leds, data);
        }

        final RenderedStagePropData dataToRender = renderedStagePropData;
        channelPropPair.getChannel().getEffects().forEach(effect -> renderEffect(sequence, dataToRender, effect));

        return renderedStagePropData;
    }

    private void renderEffect(Sequence sequence, RenderedStagePropData renderedStagePropData, Effect effect) {
        EffectTypeCode effectTypeCode = effect.getType();
        EffectRenderer renderer = EffectTypeRenderers.get(effectTypeCode);

        int startFrame = Math.max(this.startFrame, effect.getStartFrame());
        int endFrame = Math.min(this.endFrame, effect.getEndFrame());

        for (int frameNumber = startFrame; frameNumber <= endFrame; frameNumber++) {
            RenderedFrame frame = renderedStagePropData.getFrames().get(frameNumber - this.startFrame);
            renderer.render(sequence, renderedStagePropData, frame, effect);
        }
    }
}
