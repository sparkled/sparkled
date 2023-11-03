package io.sparkled.api

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.sparkled.model.UniqueId
import io.sparkled.persistence.DbService
import io.sparkled.persistence.repository.findByIdOrNull
import io.sparkled.viewmodel.StageSummaryViewModel
import io.sparkled.viewmodel.StageViewModel
import jakarta.transaction.Transactional

@ExecuteOn(TaskExecutors.IO)
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/stages")
class StageController(
    private val db: DbService,
    private val objectMapper: ObjectMapper,
) {

    @Get("/")
    @Transactional
    fun getAllStages(): HttpResponse<Any> {
        val viewModels = db.stages.findAll().sortedBy { it.name }.map {
            StageSummaryViewModel.fromModel(it)
        }
        return HttpResponse.ok(viewModels)
    }

    @Get("/{id}")
    @Transactional
    fun getStage(id: UniqueId): HttpResponse<Any> {
        val stage = db.stages.findByIdOrNull(id)

        return if (stage == null) {
            HttpResponse.notFound("Stage not found.")
        } else {
            val stageProps = db.stageProps.findAllByStageId(id)
            val viewModel = StageViewModel.fromModel(stage, stageProps, objectMapper)
            HttpResponse.ok(viewModel)
        }
    }

    @Post("/")
    @Transactional
    fun createStage(stageViewModel: StageViewModel): HttpResponse<Any> {
        val (stage) = stageViewModel.toModel(objectMapper)
        val created = db.stages.save(stage)
        return HttpResponse.created(created)
    }

    @Put("/{id}")
    @Transactional
    fun updateStage(id: UniqueId, stageViewModel: StageViewModel): HttpResponse<Any> {
        val stageAndStageProps = stageViewModel.copy(id = id).toModel(objectMapper)
        val stage = stageAndStageProps.first.copy(id = id)

        // Update stage.
        db.stages.update(stage)

        val existingStageProps = db.stageProps.findAllByStageId(id).associateBy { it.id }
        val newStageProps = stageAndStageProps.second.map { it.copy(stageId = id) }.associateBy { it.id }

        // Delete stage props that no longer exist.
        val idsToDelete = existingStageProps.keys - newStageProps.keys
        idsToDelete.forEach(db.stageProps::deleteById)

        // Insert stage props that didn't exist previously.
        val idsToInsert = newStageProps.keys - existingStageProps.keys
        idsToInsert.forEach { db.stageProps.save(newStageProps.getValue(it).copy(stageId = stage.id)) }

        // Update stage props that exist
        val idsToUpdate = newStageProps.keys.intersect(existingStageProps.keys)
        idsToUpdate.forEach { db.stageProps.update(newStageProps.getValue(it).copy(stageId = stage.id)) }

        return HttpResponse.ok()
    }

    @Delete("/{id}")
    @Transactional
    fun deleteStage(id: UniqueId): HttpResponse<Any> {
        db.stages.deleteStageAndDependentsById(id)
        return HttpResponse.noContent()
    }
}
