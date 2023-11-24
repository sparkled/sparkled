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
import io.micronaut.transaction.annotation.Transactional
import io.sparkled.model.ScheduledActionModel
import io.sparkled.model.UniqueId
import io.sparkled.persistence.DbService
import io.sparkled.persistence.repository.findByIdOrNull
import io.sparkled.scheduler.SchedulerService
import io.sparkled.viewmodel.ScheduledActionEditViewModel
import io.sparkled.viewmodel.ScheduledActionViewModel
import io.sparkled.viewmodel.ScheduledTaskSummaryViewModel
import io.sparkled.viewmodel.error.ApiErrorCode
import io.sparkled.viewmodel.exception.HttpResponseException

@ExecuteOn(TaskExecutors.BLOCKING)
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
        val existing = db.scheduledActions.findByIdOrNull(id)
            ?: throw HttpResponseException(ApiErrorCode.ERR_NOT_FOUND)

        val viewModel = ScheduledActionViewModel.fromModel(existing)
        return HttpResponse.ok(viewModel)
    }

    @Post("/")
    @Transactional
    fun createScheduledTask(
        @Body body: ScheduledActionEditViewModel,
    ): HttpResponse<Any> {
        val scheduledJob = ScheduledActionModel(
            type = body.type,
            cronExpression = body.cronExpression,
            value = body.value,
            playlistId = body.playlistId,
        )

        val saved = db.scheduledActions.save(scheduledJob)
        schedulerService.reload()

        val viewModel = ScheduledActionViewModel.fromModel(saved)
        return HttpResponse.created(viewModel)
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
