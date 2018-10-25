package io.sparkled.rest.service.stage

import io.sparkled.persistence.stage.StagePersistenceService
import io.sparkled.persistence.transaction.Transaction
import io.sparkled.rest.response.IdResponse
import io.sparkled.rest.service.RestServiceHandler
import io.sparkled.viewmodel.stage.StageViewModel
import io.sparkled.viewmodel.stage.StageViewModelConverter
import io.sparkled.viewmodel.stage.prop.StagePropViewModelConverter
import io.sparkled.viewmodel.stage.search.StageSearchViewModelConverter
import javax.inject.Inject
import javax.inject.Provider
import javax.persistence.EntityManager
import javax.ws.rs.core.Response

open class StageRestServiceHandler @Inject
constructor(private val entityManagerProvider: Provider<EntityManager>,
            private val stagePersistenceService: StagePersistenceService,
            private val stageViewModelConverter: StageViewModelConverter,
            private val stageSearchViewModelConverter: StageSearchViewModelConverter,
            private val stagePropViewModelConverter: StagePropViewModelConverter) : RestServiceHandler() {

    fun createStage(stageViewModel: StageViewModel): Response {
        return Transaction(entityManagerProvider).of {
            var stage = stageViewModelConverter.toModel(stageViewModel)
            stage = stagePersistenceService.createStage(stage)
            return@of respondOk(IdResponse(stage.getId()!!))
        }
    }

    fun getAllStages(): Response {
        val stages = stagePersistenceService.getAllStages()
        val viewModels = stageSearchViewModelConverter.toViewModels(stages)
        return respondOk(viewModels)
    }

    fun getStage(stageId: Int): Response {
        val stageOptional = stagePersistenceService.getStageById(stageId)

        if (stageOptional.isPresent) {
            val stage = stageOptional.get()
            val viewModel = stageViewModelConverter.toViewModel(stage)

            val stageProps = stagePersistenceService
                    .getStagePropsByStageId(stageId)
                    .asSequence()
                    .map(stagePropViewModelConverter::toViewModel)
                    .toList()
            viewModel.setStageProps(stageProps)

            return respondOk(viewModel)
        }

        return respond(Response.Status.NOT_FOUND, "Stage not found.")
    }

    fun updateStage(id: Int, stageViewModel: StageViewModel): Response {
        return Transaction(entityManagerProvider).of {
            stageViewModel.setId(id) // Prevent client-side ID tampering.

            val stage = stageViewModelConverter.toModel(stageViewModel)
            val stageProps = stageViewModel.getStageProps()
                    .asSequence()
                    .map(stagePropViewModelConverter::toModel)
                    .map { it.setStageId(id) }
                    .toList()

            stagePersistenceService.saveStage(stage, stageProps)
            return@of respondOk()
        }
    }

    fun deleteStage(id: Int): Response {
        return Transaction(entityManagerProvider).of {
            stagePersistenceService.deleteStage(id)
            return@of respondOk()
        }
    }
}