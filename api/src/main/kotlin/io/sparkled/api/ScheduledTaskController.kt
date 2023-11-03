package io.sparkled.api

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.sparkled.model.UniqueId
import io.sparkled.persistence.DbService
import io.sparkled.persistence.repository.findByIdOrNull
import io.sparkled.scheduler.SchedulerService
import io.sparkled.viewmodel.ScheduledActionViewModel
import io.sparkled.viewmodel.ScheduledTaskSummaryViewModel
import jakarta.transaction.Transactional

@ExecuteOn(TaskExecutors.IO)
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/scheduledTasks")
class ScheduledTaskController(
    private val db: DbService,
    private val schedulerService: SchedulerService,
) {

    @Get("/")
    @Transactional
    fun getAllScheduledTasks(): HttpResponse<Any> {
        val playlists = db.playlists.findAll().sortedBy { it.name }
        val playlistNames = playlists.associate { it.id to it.name }

        val scheduledJobs = db.scheduledActions.findAll().sortedBy { it.createdAt }.map {
            ScheduledTaskSummaryViewModel.fromModel(it, playlistNames)
        }

        return HttpResponse.ok(scheduledJobs)
    }

    @Get("/{id}")
    @Transactional
    fun getScheduledTask(
        @PathVariable id: UniqueId,
    ): HttpResponse<Any> {
        val viewModel = db.scheduledActions.findByIdOrNull(id)?.let {
            ScheduledActionViewModel.fromModel(it)
        }

        return when {
            null != viewModel -> HttpResponse.ok(viewModel)
            else -> HttpResponse.notFound("scheduled task not found.")
        }
    }

    @Post("/")
    @Transactional
    fun createScheduledTask(
        @Body body: ScheduledActionViewModel,
    ): HttpResponse<Any> {
        val scheduledJob = body.toModel()
        val saved = db.scheduledActions.save(scheduledJob)
        schedulerService.reload()
        return HttpResponse.created(saved) // TODO viewmodel
    }

    @Delete("/{id}")
    @Transactional
    fun deleteScheduledTask(
        @PathVariable id: UniqueId,
    ): HttpResponse<Any> {
        db.scheduledActions.deleteById(id)
        schedulerService.reload()
        return HttpResponse.noContent()
    }
}
