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

    List<Sequence> getAllSequences();

    Integer deleteSequence(int sequenceId);

    Optional<Sequence> getSequenceById(int sequenceId);

    Optional<Stage> getStageBySequenceId(int sequenceId);

    Optional<SongAudio> getSongAudioBySequenceId(int sequenceId);

    Integer createSequence(Sequence sequence, byte[] songAudioData);

    Integer saveSequence(Sequence sequence, List<SequenceChannel> sequenceChannels);

    Integer publishSequence(Sequence sequence, Stage stage, List<SequenceChannel> sequenceChannels, RenderedStagePropDataMap renderedStageProps);

    RenderedStagePropDataMap getRenderedStageProps(Sequence sequence);

    Optional<SequenceChannel> getSequenceChannelByUuid(int sequenceId, UUID uuid);

    List<SequenceChannel> getSequenceChannelsBySequenceId(int sequenceId);
}
