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
import io.sparkled.renderer.util.ChannelPropPairUtil;
import io.sparkled.renderer.util.EffectTypeRenderers;

import java.util.List;

public class Renderer {

    private final Sequence sequence;
    private final List<ChannelPropPair> channelPropPairs;
    private final int startFrame;
    private final int durationFrames;

    public Renderer(Sequence sequence, List<SequenceChannel> sequenceChannels, List<StageProp> stageProps) {
        this(sequence, sequenceChannels, stageProps, 0, sequence.getDurationFrames());
    }

    public Renderer(Sequence sequence, List<SequenceChannel> sequenceChannels, List<StageProp> stageProps, int startFrame, int durationFrames) {
        this.sequence = sequence;
        this.channelPropPairs = ChannelPropPairUtil.makePairs(sequenceChannels, stageProps);
        this.startFrame = startFrame;
        this.durationFrames = durationFrames;
    }

    public RenderedStagePropDataMap render() {
        RenderedStagePropDataMap renderedProps = new RenderedStagePropDataMap();

        channelPropPairs.forEach(cpp -> {
                    String stagePropCode = cpp.getStageProp().getCode();
                    RenderedStagePropData data = renderedProps.get(stagePropCode);
                    renderedProps.put(stagePropCode, renderChannel(cpp, data));
                }
        );
        return renderedProps;
    }

    private RenderedStagePropData renderChannel(ChannelPropPair channelPropPair, RenderedStagePropData renderedStagePropData) {
        if (renderedStagePropData == null) {
            final int endFrame = Math.min(sequence.getDurationFrames() - 1, startFrame + durationFrames - 1);

            int frameCount = endFrame - startFrame + 1;
            int leds = channelPropPair.getStageProp().getLeds();
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
        int endFrame = Math.min(this.startFrame + (this.durationFrames - 1), effect.getEndFrame());

        for (int frameNumber = startFrame; frameNumber <= endFrame; frameNumber++) {
            RenderedFrame frame = renderedStagePropData.getFrames().get(frameNumber - this.startFrame);
            renderer.render(sequence, renderedStagePropData, frame, effect);
        }
    }
}
