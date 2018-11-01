package io.sparkled.persistence.scheduledjob.impl.query

import io.sparkled.model.entity.ScheduledJob
import io.sparkled.model.validator.ScheduledJobValidator
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import org.slf4j.LoggerFactory

class SaveScheduledJobQuery(private val scheduledJob: ScheduledJob) : PersistenceQuery<ScheduledJob> {

    override fun perform(queryFactory: QueryFactory): ScheduledJob {
        ScheduledJobValidator().validate(scheduledJob)

        val entityManager = queryFactory.entityManager
        val savedScheduledJob = entityManager.merge(scheduledJob)
        logger.info("Saved scheduled job {} ({}).", savedScheduledJob.id, savedScheduledJob.cronExpression)
        return savedScheduledJob!!
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SaveScheduledJobQuery::class.java)
    }
}
