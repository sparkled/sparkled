package io.sparkled.persistence.sequence;

import io.sparkled.model.entity.*;
import io.sparkled.model.render.RenderedStagePropDataMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface SequencePersistenceService {

    Sequence createSequence(Sequence sequence);

    List<Sequence> getAllSequences();

    Optional<Sequence> getSequenceById(int sequenceId);

    Optional<Stage> getStageBySequenceId(int sequenceId);

    Optional<SongAudio> getSongAudioBySequenceId(int sequenceId);

    List<SequenceChannel> getSequenceChannelsBySequenceId(int sequenceId);

    Optional<SequenceChannel> getSequenceChannelByUuid(int sequenceId, UUID uuid);

    RenderedStagePropDataMap getRenderedStagePropsBySequenceAndSong(Sequence sequence, Song song);

    Map<String, UUID> getSequenceStagePropUuidMapBySequenceId(int sequenceId);

    void saveSequence(Sequence sequence, List<SequenceChannel> sequenceChannels);

    void publishSequence(Sequence sequence, List<SequenceChannel> sequenceChannels, RenderedStagePropDataMap renderedStageProps);

    void deleteSequence(int sequenceId);
}
