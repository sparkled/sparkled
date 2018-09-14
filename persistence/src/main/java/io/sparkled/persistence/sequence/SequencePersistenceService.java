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

    Integer saveSequence(Sequence sequence);

    Integer saveSongAudio(SongAudio songAudio);

    void saveSequenceChannels(List<SequenceChannel> sequenceChannels);

    RenderedStagePropDataMap getRenderedStageProps(Sequence sequence);

    void saveRenderedChannels(Sequence sequence, RenderedStagePropDataMap renderedStagePropDataMap);

    void deleteRenderedStageProps(int sequenceId);

    Optional<SequenceChannel> getSequenceChannelByUuid(int sequenceId, UUID uuid);

    List<SequenceChannel> getSequenceChannelsBySequenceId(int sequenceId);
}
