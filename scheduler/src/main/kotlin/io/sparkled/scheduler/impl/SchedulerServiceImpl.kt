package io.sparkled.scheduler.impl

import io.sparkled.common.logging.getLogger
import io.sparkled.model.ScheduledActionModel
import io.sparkled.model.SettingModel
import io.sparkled.model.enumeration.ScheduledActionType
import io.sparkled.model.setting.SettingsConstants
import io.sparkled.music.PlaybackService
import io.sparkled.persistence.DbService
import io.sparkled.scheduler.SchedulerService
import jakarta.inject.Singleton
import io.micronaut.transaction.annotation.Transactional
import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.JobDataMap
import org.quartz.Scheduler
import org.quartz.SchedulerException
import org.quartz.TriggerBuilder
import org.quartz.impl.StdSchedulerFactory

@Singleton
class SchedulerServiceImpl(
    private val db: DbService,
    private val playbackService: PlaybackService,
) : SchedulerService {

    private val scheduler: Scheduler = StdSchedulerFactory.getDefaultScheduler()

    override fun start() {
        try {
            scheduler.start()
            logger.info("Scheduler started successfully.")

            reload()
        } catch (e: SchedulerException) {
            logger.error("Failed to start scheduler.", e)
        }
    }

    override fun stop() {
        scheduler.shutdown()
    }

    override fun reload() {
        scheduler.clear()
        logger.info("Cleared existing scheduled task(s).")

        val scheduledJobs = db.scheduledActions.findAll().toList()
        scheduledJobs.forEach {
            try {
                schedule(it)
            } catch (e: RuntimeException) {
                logger.error("Failed to schedule action.", e, "id" to it.id)
            }
        }

        logger.info("Started scheduled action.", "count" to scheduledJobs.size)
    }

    private fun schedule(scheduledJob: ScheduledActionModel) {
        val jobDataMap = JobDataMap(
            mapOf(
                JobDelegator.SERVICE to this,
                JobDelegator.JOB to scheduledJob,
            ),
        )

        val jobDetail = JobBuilder.newJob(JobDelegator::class.java)
            .setJobData(jobDataMap)
            .build()

        val trigger = TriggerBuilder.newTrigger()
            .startNow()
            .withSchedule(CronScheduleBuilder.cronSchedule(scheduledJob.cronExpression))
            .build()

        scheduler.scheduleJob(jobDetail, trigger)
    }

    internal fun handleTask(task: ScheduledActionModel) {
        logger.info("Executing scheduled task.", "id" to task.id, "type" to task.type)

        when (val action = task.type) {
            ScheduledActionType.PLAY_PLAYLIST -> playPlaylist(task)
            ScheduledActionType.STOP_PLAYBACK -> stopPlayback()
            ScheduledActionType.SET_BRIGHTNESS -> setBrightness(task)
            ScheduledActionType.NONE -> logger.warn("Unrecognised scheduler action, skipping.", "action" to action)
        }
    }

    @Synchronized
    @Transactional
    fun playPlaylist(job: ScheduledActionModel) {
        val sequences = db.sequences.findAllByPlaylistId(job.playlistId ?: "")
        playbackService.play(sequences, true) // TODO: Allow repeat config via scheduler API.
    }

    @Synchronized
    private fun stopPlayback() {
        playbackService.stopPlayback()
    }

    @Synchronized
    @Transactional
    fun setBrightness(job: ScheduledActionModel) {
        val brightness = (job.value ?: "0")
        // TODO create or update
        val setting = SettingModel(id = SettingsConstants.Brightness.CODE, value = brightness)
        db.settings.update(setting)
    }

    companion object {
        private val logger = getLogger<SchedulerServiceImpl>()
    }
}
