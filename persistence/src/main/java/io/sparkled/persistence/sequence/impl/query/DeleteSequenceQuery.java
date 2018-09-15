package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.*;
import io.sparkled.persistence.BulkDeleteQuery;
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
        deleteSongAudio(entityManager);
        deleteSequenceChannels(entityManager);
        deleteScheduledSequences(entityManager);
        deleteRenderedStageProps(entityManager);
        deleteSequence(entityManager);

        return sequenceId;
    }

    private void deleteSongAudio(EntityManager entityManager) {
        BulkDeleteQuery.from(SongAudio.class).where(SongAudio_.sequenceId).is(sequenceId).perform(entityManager);
    }

    private void deleteSequenceChannels(EntityManager entityManager) {
        BulkDeleteQuery.from(SequenceChannel.class).where(SequenceChannel_.sequenceId).is(sequenceId).perform(entityManager);
    }

    private void deleteScheduledSequences(EntityManager entityManager) {
        List<ScheduledSequence> scheduledSequences = new GetScheduledSequencesBySequenceIdQuery(sequenceId).perform(entityManager);
        scheduledSequences.forEach(entityManager::remove);
    }

    private void deleteRenderedStageProps(EntityManager entityManager) {
        BulkDeleteQuery.from(RenderedStageProp.class).where(RenderedStageProp_.sequenceId).is(sequenceId).perform(entityManager);
    }

    private void deleteSequence(EntityManager entityManager) {
        BulkDeleteQuery.from(Sequence.class).where(Sequence_.id).is(sequenceId).perform(entityManager);
    }
}
