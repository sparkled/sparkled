package io.sparkled.model.animation;

import io.sparkled.model.entity.StageProp;

/**
 * Pairs up a sequence channel with a stage prop.
 */
public class ChannelPropPair {

    private SequenceChannelEffects channel;
    private StageProp stageProp;

    public ChannelPropPair(SequenceChannelEffects channel, StageProp stageProp) {
        this.channel = channel;
        this.stageProp = stageProp;
    }

    public SequenceChannelEffects getChannel() {
        return channel;
    }

    public StageProp getStageProp() {
        return stageProp;
    }
}
