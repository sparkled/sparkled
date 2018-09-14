package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.*;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.scheduler.impl.query.GetScheduledSequencesBySequenceIdQuery;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class DeleteSequenceQuery implements PersistenceQuery<Integer> {

    private final int sequenceId;

    public DeleteSequenceQuery(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public Integer perform(EntityManager entityManager) {
        Optional<SongAudio> songAudio = new GetSongAudioBySequenceIdQuery(sequenceId).perform(entityManager);
        songAudio.ifPresent(entityManager::remove);

        List<SequenceChannel> channels = new GetSequenceChannelsBySequenceIdQuery(sequenceId).perform(entityManager);
        channels.forEach(entityManager::remove);

        List<ScheduledSequence> scheduledSequences = new GetScheduledSequencesBySequenceIdQuery(sequenceId).perform(entityManager);
        scheduledSequences.forEach(entityManager::remove);

        new DeleteRenderedStagePropsQuery(sequenceId).perform(entityManager);

        Optional<Sequence> sequence = new GetSequenceByIdQuery(sequenceId).perform(entityManager);
        sequence.ifPresent(entityManager::remove);

        return sequenceId;
    }
}
