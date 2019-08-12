package io.sparkled.persistence.scheduledjob.impl.query

import io.sparkled.model.entity.QScheduledJob.Companion.scheduledJob
import io.sparkled.model.entity.ScheduledJob
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetScheduledJobsQuery : PersistenceQuery<List<ScheduledJob>> {

    override fun perform(queryFactory: QueryFactory): List<ScheduledJob> {
        return queryFactory
            .selectFrom(scheduledJob)
            .fetch()
    }
}
