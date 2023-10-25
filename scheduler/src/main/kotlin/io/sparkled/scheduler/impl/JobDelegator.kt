package io.sparkled.scheduler.impl

import io.sparkled.model.ScheduledActionModel
import org.quartz.Job
import org.quartz.JobExecutionContext

/**
 * Responsible for passing scheduled tasks executions back to the scheduler for processing.
 */
class JobDelegator : Job {

    override fun execute(context: JobExecutionContext) {
        val jobDataMap = context.jobDetail.jobDataMap
        val service: SchedulerServiceImpl = jobDataMap[SERVICE] as SchedulerServiceImpl
        val job = jobDataMap[JOB] as ScheduledActionModel
        service.handleTask(job)
    }

    companion object {
        const val SERVICE = "SERVICE"
        const val JOB = "JOB"
    }
}
