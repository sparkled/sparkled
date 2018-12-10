package io.sparkled.scheduler.impl

import io.sparkled.model.entity.ScheduledJob
import org.quartz.Job
import org.quartz.JobExecutionContext

/**
 * Responsible for passing scheduled jobs executions back to the scheduler for processing.
 */
internal class JobDelegator : Job {

    override fun execute(context: JobExecutionContext) {
        val jobDataMap = context.jobDetail.jobDataMap
        val service: SchedulerServiceImpl = jobDataMap[SERVICE] as SchedulerServiceImpl
        val job: ScheduledJob = jobDataMap[JOB] as ScheduledJob

        service.handleJob(job)
    }

    companion object Keys {
        const val SERVICE = "SERVICE"
        const val JOB = "JOB"
    }
}
