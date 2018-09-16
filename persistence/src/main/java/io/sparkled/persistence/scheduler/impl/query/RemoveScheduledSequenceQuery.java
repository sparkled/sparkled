package io.sparkled.persistence.scheduler.impl.query;

import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

public class RemoveScheduledSequenceQuery implements PersistenceQuery<Boolean> {

    private final int scheduledSequenceId;

    public RemoveScheduledSequenceQuery(int scheduledSequenceId) {
        this.scheduledSequenceId = scheduledSequenceId;
    }

    @Override
    public Boolean perform(QueryFactory queryFactory) {
        return queryFactory
                .delete(qScheduledSequence)
                .where(qScheduledSequence.id.eq(scheduledSequenceId))
                .execute() > 0;
    }
}
