package io.sparkled.persistence.scheduler.impl.query;

import io.sparkled.model.entity.ScheduledSequence;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.Date;
import java.util.Optional;

public class GetNextScheduledSequenceQuery implements PersistenceQuery<Optional<ScheduledSequence>> {

    @Override
    public Optional<ScheduledSequence> perform(QueryFactory queryFactory) {
        ScheduledSequence scheduledSequence = queryFactory
                .selectFrom(qScheduledSequence)
                .where(qScheduledSequence.startTime.gt(new Date()))
                .orderBy(qScheduledSequence.startTime.asc())
                .fetchFirst();

        return Optional.ofNullable(scheduledSequence);
    }
}
