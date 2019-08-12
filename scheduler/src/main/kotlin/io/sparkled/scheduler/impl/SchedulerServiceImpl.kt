package io.sparkled.scheduler.impl

import io.micronaut.spring.tx.annotation.Transactional
import io.sparkled.model.entity.ScheduledJob
import io.sparkled.model.entity.ScheduledJobAction
import io.sparkled.music.PlaybackService
import io.sparkled.persistence.playlist.PlaylistPersistenceService
import io.sparkled.persistence.scheduledjob.ScheduledJobPersistenceService
import io.sparkled.persistence.setting.SettingPersistenceService
import io.sparkled.scheduler.SchedulerService
import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.JobDataMap
import org.quartz.Scheduler
import org.quartz.SchedulerException
import org.quartz.TriggerBuilder
import org.quartz.impl.StdSchedulerFactory
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
open class SchedulerServiceImpl(
    private val scheduledJobPersistenceService: ScheduledJobPersistenceService,
    private val playlistPersistenceService: PlaylistPersistenceService,
    private val settingPersistenceService: SettingPersistenceService,
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
        logger.info("Cleared existing scheduled job(s).")

        val scheduledJobs = scheduledJobPersistenceService.getAllScheduledJobs()
        scheduledJobs.forEach {
            try {
                schedule(it)
            } catch (e: RuntimeException) {
                logger.error("Failed to schedule job {}.", it.id, e)
            }
        }

        logger.info("Started {} scheduled job(s).", scheduledJobs.size)
    }

    private fun schedule(scheduledJob: ScheduledJob) {
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

    internal fun handleJob(job: ScheduledJob) {
        logger.info("Executing scheduled job {}.", job.id)

        when (val action = job.action!!) {
            ScheduledJobAction.PLAY_PLAYLIST -> playPlaylist(job)
            ScheduledJobAction.STOP_PLAYBACK -> stopPlayback()
            ScheduledJobAction.SET_BRIGHTNESS -> setBrightness(job)
            else -> logger.warn("Unrecognised scheduler action {}, skipping.", action)
        }
    }

    @Synchronized
    private fun playPlaylist(job: ScheduledJob) {
        val playlist = playlistPersistenceService.getPlaylistById(job.playlistId!!)
        playlist?.apply(playbackService::play)
    }

    @Synchronized
    private fun stopPlayback() {
        playbackService.stopPlayback()
    }

    @Synchronized
    @Transactional
    open fun setBrightness(job: ScheduledJob) {
        val brightness = (job.value ?: "0")
        settingPersistenceService.setBrightness(brightness)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SchedulerServiceImpl::class.java)
    }
}
