package io.sparkled.rest

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.sparkled.model.entity.v2.StageEntity
import io.sparkled.persistence.*
import io.sparkled.persistence.query.stage.DeleteStagePropsQuery
import io.sparkled.persistence.query.stage.DeleteStageQuery
import io.sparkled.persistence.query.stage.GetStagePropsByStageIdQuery
import io.sparkled.rest.response.IdResponse
import io.sparkled.viewmodel.StageSummaryViewModel
import io.sparkled.viewmodel.StageViewModel
import org.springframework.transaction.annotation.Transactional

@ExecuteOn(TaskExecutors.IO)
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/stages")
class StageController(
    private val db: DbService,
    private val objectMapper: ObjectMapper,
) {

    @Get("/")
    @Transactional(readOnly = true)
    fun getAllStages(): HttpResponse<Any> {
        val viewModels = db.getAll<StageEntity>(orderBy = "name").map {
            StageSummaryViewModel.fromModel(it)
        }
        return HttpResponse.ok(viewModels)
    }

    @Get("/{id}")
    @Transactional(readOnly = true)
    fun getStage(id: Int): HttpResponse<Any> {
        val stage = db.getById<StageEntity>(id)

        return if (stage != null) {
            val stageProps = db.query(GetStagePropsByStageIdQuery(id))
            val viewModel = StageViewModel.fromModel(stage, stageProps, objectMapper)
            HttpResponse.ok(viewModel)
        } else HttpResponse.notFound("Stage not found.")
    }

    @Post("/")
    @Transactional
    fun createStage(stageViewModel: StageViewModel): HttpResponse<Any> {
        val (stage) = stageViewModel.toModel(objectMapper)
        val stageId = db.insert(stage)
        return HttpResponse.ok(IdResponse(stageId.toInt()))
    }

    @Put("/{id}")
    @Transactional
    fun updateStage(id: Int, stageViewModel: StageViewModel): HttpResponse<Any> {
        val stageAndStageProps = stageViewModel.copy(id = id).toModel(objectMapper)
        val stage = stageAndStageProps.first.copy(id = id)

        // Update stage.
        db.update(stage)

        val existingStageProps = db.query(GetStagePropsByStageIdQuery(id)).associateBy { it.uuid }
        val newStageProps = stageAndStageProps.second.map { it.copy(stageId = id) }.associateBy { it.uuid }

        // Delete playlist sequences that no longer exist.
        val uuidsToDelete = existingStageProps.keys - newStageProps.keys
        db.query(DeleteStagePropsQuery(uuidsToDelete))

        // Insert playlist sequences that didn't exist previously.
        (newStageProps.keys - existingStageProps.keys).forEach { db.insert(newStageProps.getValue(it).copy(stageId = stage.id)) }

        // Update playlist sequences that exist
        (newStageProps.keys.intersect(existingStageProps.keys)).forEach { db.update(newStageProps.getValue(it).copy(stageId = stage.id)) }

        return HttpResponse.ok()
    }

    @Delete("/{id}")
    @Transactional
    fun deleteStage(id: Int): HttpResponse<Any> {
        db.query(DeleteStageQuery(id))
        return HttpResponse.ok()
    }
}
