package io.sparkled.rest

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.spring.tx.annotation.Transactional
import io.sparkled.persistence.scheduledjob.ScheduledJobPersistenceService
import io.sparkled.rest.response.IdResponse
import io.sparkled.scheduler.SchedulerService
import io.sparkled.viewmodel.scheduledjob.ScheduledJobViewModel
import io.sparkled.viewmodel.scheduledjob.ScheduledJobViewModelConverter
import io.sparkled.viewmodel.scheduledjob.search.ScheduledJobSearchViewModelConverter

@Controller("/api/scheduledJobs")
open class ScheduledJobController(
    private val schedulerService: SchedulerService,
    private val scheduledJobPersistenceService: ScheduledJobPersistenceService,
    private val scheduledJobSearchViewModelConverter: ScheduledJobSearchViewModelConverter,
    private val scheduledJobViewModelConverter: ScheduledJobViewModelConverter
) {

    @Get("/")
    @Transactional(readOnly = true)
    open fun getAllScheduledJobs(): HttpResponse<Any> {
        val scheduledJobs = scheduledJobPersistenceService.getAllScheduledJobs()
        return HttpResponse.ok(scheduledJobSearchViewModelConverter.toViewModels(scheduledJobs))
    }

    @Get("/{id}")
    @Transactional(readOnly = true)
    open fun getScheduledJob(id: Int): HttpResponse<Any> {
        val scheduledJob = scheduledJobPersistenceService.getScheduledJobById(id)

        if (scheduledJob != null) {
            val viewModel = scheduledJobViewModelConverter.toViewModel(scheduledJob)
            return HttpResponse.ok(viewModel)
        }

        return HttpResponse.notFound("Scheduled job not found.")
    }

    @Post("/")
    @Transactional
    open fun createScheduledJob(scheduledJobViewModel: ScheduledJobViewModel): HttpResponse<Any> {
        val scheduledJob = scheduledJobViewModelConverter.toModel(scheduledJobViewModel)
        val savedScheduledJob = scheduledJobPersistenceService.createScheduledJob(scheduledJob)
        schedulerService.reload()
        return HttpResponse.ok(IdResponse(savedScheduledJob.id!!))
    }

    @Delete("/{id}")
    @Transactional
    open fun deleteScheduledJob(id: Int): HttpResponse<Any> {
        scheduledJobPersistenceService.deleteScheduledJob(id)
        schedulerService.reload()
        return HttpResponse.ok()
    }
}
