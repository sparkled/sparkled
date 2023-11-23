package io.sparkled.api

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.sparkled.model.StageModel
import io.sparkled.model.UniqueId
import io.sparkled.persistence.DbService
import io.sparkled.persistence.repository.findByIdOrNull
import io.sparkled.viewmodel.StageEditViewModel
import io.sparkled.viewmodel.StageSummaryViewModel
import io.sparkled.viewmodel.StageViewModel
import io.sparkled.viewmodel.error.ApiErrorCode
import io.sparkled.viewmodel.exception.HttpResponseException
import jakarta.transaction.Transactional

@ExecuteOn(TaskExecutors.IO)
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/stages")
class StageController(
    private val db: DbService,
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
            val viewModel = StageViewModel.fromModel(stage, stageProps)
            HttpResponse.ok(viewModel)
        }
    }

    @Post("/")
    @Transactional
    fun createStage(
        @Body body: StageEditViewModel
    ): HttpResponse<Any> {
        val stage = StageModel(
            name = body.name,
            width = body.width,
            height = body.height,
        )

        val created = db.stages.save(stage)
        val viewModel = StageViewModel.fromModel(created, emptyList())
        return HttpResponse.created(viewModel)
    }

    @Put("/{id}")
    @Transactional
    fun updateStage(
        @PathVariable id: UniqueId,
        @Body body: StageEditViewModel,
    ): HttpResponse<Any> {
        val stage = db.stages.findByIdOrNull(id)
            ?: throw HttpResponseException(ApiErrorCode.ERR_NOT_FOUND)

        // Update stage.
        val updatedStage = db.stages.update(
            stage.copy(
                name = body.name,
                width = body.width,
                height = body.height,
            )
        )

        val existingStageProps = db.stageProps.findAllByStageId(id).associateBy { it.id }
        val newStageProps = body.stageProps.map { it.toModel().copy(stageId = id) }.associateBy { it.id }

        // Delete stage props that no longer exist.
        val idsToDelete = existingStageProps.keys - newStageProps.keys
        idsToDelete.forEach(db.stageProps::deleteById)

        // Insert stage props that didn't exist previously.
        val idsToInsert = newStageProps.keys - existingStageProps.keys
        idsToInsert.forEach { db.stageProps.save(newStageProps.getValue(it).copy(stageId = stage.id)) }

        // Update stage props that exist
        val idsToUpdate = newStageProps.keys.intersect(existingStageProps.keys)
        idsToUpdate.forEach { db.stageProps.update(newStageProps.getValue(it).copy(stageId = stage.id)) }

        val viewModel = StageViewModel.fromModel(updatedStage, newStageProps.values)
        return HttpResponse.ok(viewModel)
    }

    @Delete("/{id}")
    @Transactional
    fun deleteStage(id: UniqueId): HttpResponse<Any> {
        db.stages.deleteStageAndDependentsById(id)
        return HttpResponse.noContent()
    }
}
