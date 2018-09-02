package io.sparkled.persistence.scheduler.impl.query;

import io.sparkled.model.entity.ScheduledSequence;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import java.util.Optional;

public class RemoveScheduledSequenceQuery implements PersistenceQuery<Boolean> {

    private final int scheduledSequenceId;

    public RemoveScheduledSequenceQuery(int scheduledSequenceId) {
        this.scheduledSequenceId = scheduledSequenceId;
    }

    @Override
    public Boolean perform(EntityManager entityManager) {
        Optional<ScheduledSequence> scheduledSequence = new GetScheduledSequenceByIdQuery(scheduledSequenceId).perform(entityManager);
        if (scheduledSequence.isPresent()) {
            entityManager.remove(scheduledSequence.get());
            return true;
        }

        return false;
    }
}
