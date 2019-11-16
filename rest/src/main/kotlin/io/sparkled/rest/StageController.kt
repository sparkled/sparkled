package io.sparkled.rest

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.spring.tx.annotation.Transactional
import io.sparkled.persistence.stage.StagePersistenceService
import io.sparkled.rest.response.IdResponse
import io.sparkled.viewmodel.stage.StageViewModel
import io.sparkled.viewmodel.stage.StageViewModelConverter
import io.sparkled.viewmodel.stage.prop.StagePropViewModelConverter
import io.sparkled.viewmodel.stage.search.StageSearchViewModelConverter

@Controller("/api/stages")
open class StageController(
    private val stagePersistenceService: StagePersistenceService,
    private val stageSearchViewModelConverter: StageSearchViewModelConverter,
    private val stageViewModelConverter: StageViewModelConverter,
    private val stagePropViewModelConverter: StagePropViewModelConverter
) {

    @Get("/")
    @Transactional(readOnly = true)
    open fun getAllStages(): HttpResponse<Any> {
        val stages = stagePersistenceService.getAllStages()
        return HttpResponse.ok(stageSearchViewModelConverter.toViewModels(stages))
    }

    @Get("/{id}")
    @Transactional(readOnly = true)
    open fun getStage(id: Int): HttpResponse<Any> {
        val stage = stagePersistenceService.getStageById(id)

        if (stage != null) {
            val viewModel = stageViewModelConverter.toViewModel(stage)

            val stageProps = stagePersistenceService
                .getStagePropsByStageId(id)
                .asSequence()
                .map(stagePropViewModelConverter::toViewModel)
                .toList()
            viewModel.setStageProps(stageProps)

            return HttpResponse.ok(viewModel)
        }

        return HttpResponse.notFound("Stage not found.")
    }

    @Post("/")
    @Transactional
    open fun createStage(stageViewModel: StageViewModel): HttpResponse<Any> {
        val stage = stageViewModelConverter.toModel(stageViewModel)
        val savedStage = stagePersistenceService.createStage(stage)
        return HttpResponse.ok(IdResponse(savedStage.getId()!!))
    }

    @Put("/{id}")
    @Transactional
    open fun updateStage(id: Int, stageViewModel: StageViewModel): HttpResponse<Any> {
        stageViewModel.setId(id) // Prevent client-side ID tampering.

        val stage = stageViewModelConverter.toModel(stageViewModel)
        val stageProps = stageViewModel.getStageProps()
            .asSequence()
            .map(stagePropViewModelConverter::toModel)
            .map { it.setStageId(id) }
            .toList()

        stagePersistenceService.saveStage(stage, stageProps)
        return HttpResponse.ok()
    }

    @Delete("/{id}")
    @Transactional
    open fun deleteStage(id: Int): HttpResponse<Any> {
        stagePersistenceService.deleteStage(id)
        return HttpResponse.ok()
    }
}
