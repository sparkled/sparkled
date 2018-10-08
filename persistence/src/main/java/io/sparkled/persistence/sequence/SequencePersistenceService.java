package io.sparkled.persistence.sequence;

import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.SequenceChannel;
import io.sparkled.model.entity.SongAudio;
import io.sparkled.model.entity.Stage;
import io.sparkled.model.render.RenderedStagePropDataMap;

import java.util.List;
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

    RenderedStagePropDataMap getRenderedStageProps(Sequence sequence);

    Integer saveSequence(Sequence sequence, List<SequenceChannel> sequenceChannels);

    Integer publishSequence(Sequence sequence, Stage stage, List<SequenceChannel> sequenceChannels, RenderedStagePropDataMap renderedStageProps);

    void deleteSequence(int sequenceId);
}
