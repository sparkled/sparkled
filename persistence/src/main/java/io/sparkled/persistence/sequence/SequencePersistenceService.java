package io.sparkled.persistence.sequence;

import io.sparkled.model.entity.*;
import io.sparkled.model.render.RenderedStagePropDataMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface SequencePersistenceService {

    Integer createSequence(Sequence sequence, byte[] songAudioData);

    List<Sequence> getAllSequences();

    Optional<Sequence> getSequenceById(int sequenceId);

    Optional<Stage> getStageBySequenceId(int sequenceId);

    Optional<SongAudio> getSongAudioBySequenceId(int sequenceId);

    List<SequenceChannel> getSequenceChannelsBySequenceId(int sequenceId);

    Optional<SequenceChannel> getSequenceChannelByUuid(int sequenceId, UUID uuid);

    RenderedStagePropDataMap getRenderedStagePropsBySequence(Sequence sequence);

    Map<String, UUID> getSequenceStagePropUuidMapBySequenceId(Integer sequenceId);

    Sequence saveSequence(Sequence sequence, List<SequenceChannel> sequenceChannels);

    void publishSequence(Sequence sequence, List<SequenceChannel> sequenceChannels, RenderedStagePropDataMap renderedStageProps);

    void deleteSequence(int sequenceId);
}
