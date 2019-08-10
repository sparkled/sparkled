package io.sparkled.persistence.scheduledjob.impl

import io.sparkled.model.entity.ScheduledJob
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.scheduledjob.ScheduledJobPersistenceService
import io.sparkled.persistence.scheduledjob.impl.query.DeleteScheduledJobQuery
import io.sparkled.persistence.scheduledjob.impl.query.GetScheduledJobByIdQuery
import io.sparkled.persistence.scheduledjob.impl.query.GetScheduledJobsQuery
import io.sparkled.persistence.scheduledjob.impl.query.SaveScheduledJobQuery
import javax.inject.Singleton

@Singleton
class ScheduledJobPersistenceServiceImpl(private val queryFactory: QueryFactory) : ScheduledJobPersistenceService {

    override fun createScheduledJob(scheduledJob: ScheduledJob): ScheduledJob {
        return SaveScheduledJobQuery(scheduledJob).perform(queryFactory)
    }

    override fun getScheduledJobById(scheduledJobId: Int): ScheduledJob? {
        return GetScheduledJobByIdQuery(scheduledJobId).perform(queryFactory)
    }

    override fun getAllScheduledJobs(): List<ScheduledJob> {
        return GetScheduledJobsQuery().perform(queryFactory)
    }

    override fun deleteScheduledJob(id: Int) {
        DeleteScheduledJobQuery(id).perform(queryFactory)
    }
}
