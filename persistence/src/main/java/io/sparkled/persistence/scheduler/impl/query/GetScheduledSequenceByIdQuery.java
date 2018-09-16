package io.sparkled.persistence.scheduler.impl.query;

import io.sparkled.model.entity.ScheduledSequence;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.Optional;

public class GetScheduledSequenceByIdQuery implements PersistenceQuery<Optional<ScheduledSequence>> {

    private final int scheduledSequenceId;

    GetScheduledSequenceByIdQuery(int scheduledSequenceId) {
        this.scheduledSequenceId = scheduledSequenceId;
    }

    @Override
    public Optional<ScheduledSequence> perform(QueryFactory queryFactory) {
        ScheduledSequence scheduledSequence = queryFactory
                .selectFrom(qScheduledSequence)
                .where(qScheduledSequence.id.eq(scheduledSequenceId))
                .fetchFirst();

        return Optional.ofNullable(scheduledSequence);
    }
}
