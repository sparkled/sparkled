package io.sparkled.persistence.scheduler.impl.query;

import io.sparkled.model.entity.ScheduledSequence;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.Date;
import java.util.List;

public class GetScheduledSequencesQuery implements PersistenceQuery<List<ScheduledSequence>> {

    private final Date startDate;
    private final Date endDate;

    public GetScheduledSequencesQuery(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public List<ScheduledSequence> perform(QueryFactory queryFactory) {
        return queryFactory
                .selectFrom(qScheduledSequence)
                .where(qScheduledSequence.startTime.between(startDate, endDate).or(qScheduledSequence.endTime.between(startDate, endDate)))
                .orderBy(qScheduledSequence.startTime.asc())
                .fetch();
    }
}
