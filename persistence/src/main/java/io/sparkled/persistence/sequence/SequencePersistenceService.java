package io.sparkled.persistence.sequence;

import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.SequenceAnimation;
import io.sparkled.model.entity.SongAudio;
import io.sparkled.model.render.RenderedStagePropDataMap;

import java.util.List;
import java.util.Optional;

public interface SequencePersistenceService {

    List<Sequence> getAllSequences();

    Integer deleteSequence(int sequenceId);

    Optional<Sequence> getSequenceById(int sequenceId);

    Optional<SongAudio> getSongAudioBySequenceId(int sequenceId);

    Optional<SequenceAnimation> getSequenceAnimationBySequenceId(int sequenceId);

    Integer saveSequence(Sequence sequence);

    Integer saveSongAudio(SongAudio songAudio);

    Integer saveSequenceAnimation(SequenceAnimation sequenceAnimation);

    RenderedStagePropDataMap getRenderedStageProps(Sequence sequence);

    void saveRenderedChannels(Sequence sequence, RenderedStagePropDataMap renderedStagePropDataMap);

    void deleteRenderedStageProps(int sequenceId);
}
