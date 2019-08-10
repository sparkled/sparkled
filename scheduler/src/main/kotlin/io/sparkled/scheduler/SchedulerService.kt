package io.sparkled.scheduler

import io.sparkled.model.entity.ScheduledJob

/**
 * Controls the lifecycle of the [ScheduledJob] scheduler.
 */
interface SchedulerService {

    /**
     * Start the scheduler and load all [ScheduledJob]s from the database.
     */
    fun start()

    /**
     * Shuts down the scheduler.
     */
    fun stop()

    /**
     * Reload all [ScheduledJob]s from the database and restart the scheduler.
     */
    fun reload()
}
