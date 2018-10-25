package io.sparkled.persistence.stage.impl

import io.sparkled.model.entity.Stage
import io.sparkled.model.entity.StageProp
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.stage.StagePersistenceService
import io.sparkled.persistence.stage.impl.query.*

import javax.inject.Inject
import java.util.Optional
import java.util.UUID

class StagePersistenceServiceImpl @Inject
constructor(private val queryFactory: QueryFactory) : StagePersistenceService {

    @Override
    fun createStage(stage: Stage): Stage {
        return SaveStageQuery(stage).perform(queryFactory)
    }

    val allStages: List<Stage>
        @Override
        get() = GetAllStagesQuery().perform(queryFactory)

    @Override
    fun getStageById(stageId: Int): Optional<Stage> {
        return GetStageByIdQuery(stageId).perform(queryFactory)
    }

    @Override
    fun getStagePropsByStageId(stageId: Int): List<StageProp> {
        return GetStagePropsByStageIdQuery(stageId).perform(queryFactory)
    }

    @Override
    fun getStagePropByUuid(stageId: Int, uuid: UUID): Optional<StageProp> {
        return GetStagePropByUuidQuery(stageId, uuid).perform(queryFactory)
    }

    @Override
    fun saveStage(stage: Stage, stageProps: List<StageProp>) {
        var stage = stage
        stage = SaveStageQuery(stage).perform(queryFactory)
        SaveStagePropsQuery(stage, stageProps).perform(queryFactory)
    }

    @Override
    fun deleteStage(stageId: Int) {
        DeleteStageQuery(stageId).perform(queryFactory)
    }
}
