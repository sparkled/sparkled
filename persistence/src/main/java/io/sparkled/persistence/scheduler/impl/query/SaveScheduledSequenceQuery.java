package io.sparkled.persistence.scheduler.impl.query;

import io.sparkled.model.entity.ScheduledSequence;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

public class SaveScheduledSequenceQuery implements PersistenceQuery<Boolean> {

    public SaveScheduledSequenceQuery(ScheduledSequence scheduledSequence) {
    }

    @Override
    public Boolean perform(QueryFactory queryFactory) {
        return false; // TODO: I didn't bother converting this query over, as the scheduler will be rewritten soon.
    }
}
