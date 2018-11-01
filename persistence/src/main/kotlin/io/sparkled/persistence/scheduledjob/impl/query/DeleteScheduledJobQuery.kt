package io.sparkled.persistence.scheduledjob.impl.query

import io.sparkled.model.entity.QScheduledJob.scheduledJob
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import org.slf4j.LoggerFactory

class DeleteScheduledJobQuery(private val scheduledJobId: Int) : PersistenceQuery<Unit> {

    override fun perform(queryFactory: QueryFactory) {
        val deleted = queryFactory
            .delete(scheduledJob)
            .where(scheduledJob.id.eq(scheduledJobId))
            .execute()

        logger.info("Deleted {} scheduled job(s).", deleted)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DeleteScheduledJobQuery::class.java)
    }
}
