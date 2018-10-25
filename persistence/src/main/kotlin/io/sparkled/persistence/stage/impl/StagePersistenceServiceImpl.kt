package io.sparkled.persistence.stage.impl

import io.sparkled.model.entity.Stage
import io.sparkled.model.entity.StageProp
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.stage.StagePersistenceService
import io.sparkled.persistence.stage.impl.query.*
import java.util.*
import javax.inject.Inject

class StagePersistenceServiceImpl @Inject
constructor(private val queryFactory: QueryFactory) : StagePersistenceService {

    override fun createStage(stage: Stage): Stage {
        return SaveStageQuery(stage).perform(queryFactory)
    }

    override fun getAllStages(): List<Stage> {
        return GetAllStagesQuery().perform(queryFactory)
    }

    override fun getStageById(stageId: Int): Optional<Stage> {
        return GetStageByIdQuery(stageId).perform(queryFactory)
    }

    override fun getStagePropsByStageId(stageId: Int): List<StageProp> {
        return GetStagePropsByStageIdQuery(stageId).perform(queryFactory)
    }

    override fun getStagePropByUuid(stageId: Int, uuid: UUID): Optional<StageProp> {
        return GetStagePropByUuidQuery(stageId, uuid).perform(queryFactory)
    }

    override fun saveStage(stage: Stage, stageProps: List<StageProp>) {
        val savedStage = SaveStageQuery(stage).perform(queryFactory)
        SaveStagePropsQuery(savedStage, stageProps).perform(queryFactory)
    }

    override fun deleteStage(stageId: Int) {
        DeleteStageQuery(stageId).perform(queryFactory)
    }
}
