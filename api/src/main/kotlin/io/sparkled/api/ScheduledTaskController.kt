package io.sparkled.api

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.sparkled.model.entity.v2.PlaylistEntity
import io.sparkled.model.entity.v2.ScheduledTaskEntity
import io.sparkled.persistence.*
import io.sparkled.api.response.IdResponse
import io.sparkled.scheduler.SchedulerService
import io.sparkled.viewmodel.ScheduledTaskSummaryViewModel
import io.sparkled.viewmodel.ScheduledTaskViewModel
import org.springframework.transaction.annotation.Transactional

@ExecuteOn(TaskExecutors.IO)
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/scheduledTasks")
class ScheduledTaskController(
    private val db: DbService,
    private val schedulerService: SchedulerService,
) {

    @Get("/")
    @Transactional(readOnly = true)
    fun getAllScheduledTasks(): HttpResponse<Any> {
        val playlists = db.getAll<PlaylistEntity>(orderBy = "name")
        val playlistNames = playlists.associate { it.id to it.name }

        val scheduledJobs = db.getAll<ScheduledTaskEntity>(orderBy = "id").map {
            ScheduledTaskSummaryViewModel.fromModel(it, playlistNames)
        }

        return HttpResponse.ok(scheduledJobs)
    }

    @Get("/{id}")
    @Transactional(readOnly = true)
    fun getScheduledTask(id: Int): HttpResponse<Any> {
        val viewModel = db.getById<ScheduledTaskEntity>(id)?.let {
            ScheduledTaskViewModel.fromModel(it)
        }

        return if (viewModel != null) {
            HttpResponse.ok(viewModel)
        } else {
            HttpResponse.notFound("scheduled task not found.")
        }
    }

    @Post("/")
    @Transactional
    fun createScheduledTask(scheduledTaskViewModel: ScheduledTaskViewModel): HttpResponse<Any> {
        val scheduledJob = scheduledTaskViewModel.toModel()
        val savedId = db.insert(scheduledJob).toInt()
        schedulerService.reload()
        return HttpResponse.ok(IdResponse(savedId))
    }

    @Delete("/{id}")
    @Transactional
    fun deleteScheduledTask(id: Int): HttpResponse<Any> {
        db.getById<ScheduledTaskEntity>(id)?.let {
            db.delete(it)
        }

        schedulerService.reload()
        return HttpResponse.ok()
    }
}
