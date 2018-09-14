package io.sparkled.renderer.util;

import com.google.gson.Gson;
import io.sparkled.model.animation.ChannelPropPair;
import io.sparkled.model.animation.SequenceChannelEffects;
import io.sparkled.model.entity.SequenceChannel;
import io.sparkled.model.entity.StageProp;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Pairs up sequence channels with their associated stage prop.
 */
public class ChannelPropPairUtil {

    private static final Gson gson = new Gson();

    private ChannelPropPairUtil() {
    }

    public static List<ChannelPropPair> makePairs(List<SequenceChannel> sequenceChannels, List<StageProp> stageProps) {
        return sequenceChannels.stream()
                .map(sc -> getPair(sc, stageProps))
                .collect(Collectors.toList());
    }

    private static ChannelPropPair getPair(SequenceChannel sequenceChannel, List<StageProp> stageProps) {
        return new ChannelPropPair(convertChannelData(sequenceChannel), findStagePropByUuid(stageProps, sequenceChannel.getStagePropUuid()));
    }

    private static SequenceChannelEffects convertChannelData(SequenceChannel sequenceChannel) {
        return gson.fromJson(sequenceChannel.getChannelJson(), SequenceChannelEffects.class);
    }

    private static StageProp findStagePropByUuid(List<StageProp> stageProps, UUID uuid) {
        return stageProps.stream()
                .filter(sp -> sp.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }
}
