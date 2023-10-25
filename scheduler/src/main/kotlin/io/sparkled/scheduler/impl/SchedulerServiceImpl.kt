package io.sparkled.scheduler.impl

import org.springframework.transaction.annotation.Transactional
import io.sparkled.model.enumeration.ScheduledActionType
import io.sparkled.model.entity.v2.ScheduledTaskEntity
import io.sparkled.model.entity.v2.SettingEntity
import io.sparkled.model.setting.SettingsConstants
import io.sparkled.music.PlaybackService
import io.sparkled.persistence.DbService
import io.sparkled.persistence.getAll
import io.sparkled.persistence.update
import io.sparkled.persistence.query.sequence.GetSequencesByPlaylistIdQuery
import io.sparkled.scheduler.SchedulerService
import jakarta.inject.Singleton
import org.quartz.*
import org.quartz.impl.StdSchedulerFactory
import org.slf4j.LoggerFactory

@Singleton
class SchedulerServiceImpl(
    private val db: DbService,
    private val playbackService: PlaybackService
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

        val scheduledJobs = db.getAll<ScheduledTaskEntity>()
        scheduledJobs.forEach {
            try {
                schedule(it)
            } catch (e: RuntimeException) {
                logger.error("Failed to schedule job {}.", it.id, e)
            }
        }

        logger.info("Started {} scheduled task(s).", scheduledJobs.size)
    }

    private fun schedule(scheduledJob: ScheduledTaskEntity) {
        val jobDataMap = JobDataMap(
            mapOf(
                JobDelegator.SERVICE to this,
                JobDelegator.JOB to scheduledJob
            )
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

    internal fun handleTask(task: ScheduledTaskEntity) {
        logger.info("Executing scheduled task {}.", task.id)

        when (val action = task.action) {
            ScheduledActionType.PLAY_PLAYLIST -> playPlaylist(task)
            ScheduledActionType.STOP_PLAYBACK -> stopPlayback()
            ScheduledActionType.SET_BRIGHTNESS -> setBrightness(task)
            ScheduledActionType.NONE -> logger.warn("Unrecognised scheduler action {}, skipping.", action)
        }
    }

    @Synchronized
    @Transactional
    fun playPlaylist(job: ScheduledTaskEntity) {
        val sequences = db.query(GetSequencesByPlaylistIdQuery(job.playlistId ?: -1))
        playbackService.play(sequences, true) // TODO: Allow repeat config via scheduler API.
    }

    @Synchronized
    private fun stopPlayback() {
        playbackService.stopPlayback()
    }

    @Synchronized
    @Transactional
    fun setBrightness(job: ScheduledTaskEntity) {
        val brightness = (job.value ?: "0")
        val setting = SettingEntity(code = SettingsConstants.Brightness.CODE, value = brightness)
        db.update(setting, fieldsToUpdate = arrayOf("value"))
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SchedulerServiceImpl::class.java)
    }
}
