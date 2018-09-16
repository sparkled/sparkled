package io.sparkled.persistence.scheduler.impl.query;

import io.sparkled.model.entity.ScheduledSequence;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.Date;
import java.util.Optional;

public class GetScheduledSequenceAtTimeQuery implements PersistenceQuery<Optional<ScheduledSequence>> {

    private final Date time;

    public GetScheduledSequenceAtTimeQuery(Date time) {
        this.time = time;
    }

    @Override
    public Optional<ScheduledSequence> perform(QueryFactory queryFactory) {
        ScheduledSequence scheduledSequence = queryFactory
                .selectFrom(qScheduledSequence)
                .where(qScheduledSequence.startTime.loe(time).and(qScheduledSequence.endTime.goe(time)))
                .fetchFirst();

        return Optional.ofNullable(scheduledSequence);
    }
}
