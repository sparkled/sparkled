package io.sparkled.persistence.scheduledjob.impl.query

import io.sparkled.model.entity.QScheduledJob.scheduledJob
import io.sparkled.model.entity.ScheduledJob
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetScheduledJobByIdQuery(private val scheduledJobId: Int) : PersistenceQuery<ScheduledJob?> {

    override fun perform(queryFactory: QueryFactory): ScheduledJob? {
        return queryFactory
            .selectFrom<ScheduledJob?>(scheduledJob)
            .where(scheduledJob.id.eq(scheduledJobId))
            .fetchFirst()
    }
}
