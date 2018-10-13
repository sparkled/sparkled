package io.sparkled.renderer.util;

import io.sparkled.model.animation.ChannelPropPair;
import io.sparkled.model.animation.SequenceChannelEffects;
import io.sparkled.model.entity.SequenceChannel;
import io.sparkled.model.entity.StageProp;
import io.sparkled.model.util.GsonProvider;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

/**
 * Pairs up sequence channels with their associated stage prop.
 */
public class ChannelPropPairUtils {

    private ChannelPropPairUtils() {
    }

    public static List<ChannelPropPair> makePairs(List<SequenceChannel> sequenceChannels, List<StageProp> stageProps) {
        return sequenceChannels.stream()
                .map(sc -> getPair(sc, stageProps))
                .collect(toList());
    }

    private static ChannelPropPair getPair(SequenceChannel sequenceChannel, List<StageProp> stageProps) {
        return new ChannelPropPair(convertChannelData(sequenceChannel), findStagePropByUuid(stageProps, sequenceChannel.getStagePropUuid()));
    }

    private static SequenceChannelEffects convertChannelData(SequenceChannel sequenceChannel) {
        return GsonProvider.get().fromJson(sequenceChannel.getChannelJson(), SequenceChannelEffects.class);
    }

    private static StageProp findStagePropByUuid(List<StageProp> stageProps, UUID uuid) {
        return stageProps.stream()
                .filter(sp -> sp.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }
}
