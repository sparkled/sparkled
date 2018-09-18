package io.sparkled.persistence.scheduler.impl.query;

import io.sparkled.model.entity.ScheduledSequence;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.List;

public class GetScheduledSequencesBySequenceIdQuery implements PersistenceQuery<List<ScheduledSequence>> {

    private final int sequenceId;

    public GetScheduledSequencesBySequenceIdQuery(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public List<ScheduledSequence> perform(QueryFactory queryFactory) {
        return queryFactory
                .selectFrom(qScheduledSequence)
                .where(qScheduledSequence.sequence.id.eq(sequenceId))
                .fetch();
    }
}
