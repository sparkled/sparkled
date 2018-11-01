package io.sparkled.persistence.scheduledjob

import io.sparkled.model.entity.ScheduledJob

interface ScheduledJobPersistenceService {

    fun createScheduledJob(scheduledJob: ScheduledJob): ScheduledJob

    fun getAllScheduledJobs(): List<ScheduledJob>

    fun getScheduledJobById(scheduledJobId: Int): ScheduledJob?

    fun deleteScheduledJob(id: Int)
}
