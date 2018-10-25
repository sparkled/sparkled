package io.sparkled.rest.service.stage

import com.google.inject.persist.Transactional
import io.sparkled.model.entity.Stage
import io.sparkled.model.entity.StageProp
import io.sparkled.persistence.stage.StagePersistenceService
import io.sparkled.rest.response.IdResponse
import io.sparkled.rest.service.RestServiceHandler
import io.sparkled.viewmodel.stage.StageViewModel
import io.sparkled.viewmodel.stage.StageViewModelConverter
import io.sparkled.viewmodel.stage.prop.StagePropViewModel
import io.sparkled.viewmodel.stage.prop.StagePropViewModelConverter
import io.sparkled.viewmodel.stage.search.StageSearchViewModel
import io.sparkled.viewmodel.stage.search.StageSearchViewModelConverter

import javax.inject.Inject
import javax.ws.rs.core.Response
import java.util.Optional

import java.util.stream.Collectors.toList

internal class StageRestServiceHandler @Inject
constructor(private val stagePersistenceService: StagePersistenceService,
            private val stageViewModelConverter: StageViewModelConverter,
            private val stageSearchViewModelConverter: StageSearchViewModelConverter,
            private val stagePropViewModelConverter: StagePropViewModelConverter) : RestServiceHandler() {

    @Transactional
    fun createStage(stageViewModel: StageViewModel): Response {
        var stage = stageViewModelConverter.toModel(stageViewModel)
        stage = stagePersistenceService.createStage(stage)
        return respondOk(IdResponse(stage.getId()))
    }

    val allStages: Response
        get() {
            val stages = stagePersistenceService.getAllStages()
            val viewModels = stageSearchViewModelConverter.toViewModels(stages)
            return respondOk(viewModels)
        }

    fun getStage(stageId: Int): Response {
        val stageOptional = stagePersistenceService.getStageById(stageId)

        if (stageOptional.isPresent()) {
            val stage = stageOptional.get()
            val viewModel = stageViewModelConverter.toViewModel(stage)

            val stageProps = stagePersistenceService
                    .getStagePropsByStageId(stageId)
                    .stream()
                    .map(???({ stagePropViewModelConverter.toViewModel() }))
            .collect(toList())
            viewModel.setStageProps(stageProps)

            return respondOk(viewModel)
        }

        return respond(Response.Status.NOT_FOUND, "Stage not found.")
    }

    @Transactional
    fun updateStage(id: Int, stageViewModel: StageViewModel): Response {
        stageViewModel.setId(id) // Prevent client-side ID tampering.

        val stage = stageViewModelConverter.toModel(stageViewModel)
        val stageProps = stageViewModel.getStageProps()
                .stream()
                .map(???({ stagePropViewModelConverter.toModel() }))
        .map { ps -> ps.setStageId(id) }
                .collect(toList())

        stagePersistenceService.saveStage(stage, stageProps)
        return respondOk()
    }

    @Transactional
    fun deleteStage(id: Int): Response {
        stagePersistenceService.deleteStage(id)
        return respondOk()
    }
}