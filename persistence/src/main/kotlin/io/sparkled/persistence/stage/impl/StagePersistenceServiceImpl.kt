package io.sparkled.persistence.stage.impl

import io.sparkled.model.entity.Stage
import io.sparkled.model.entity.StageProp
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.stage.StagePersistenceService
import io.sparkled.persistence.stage.impl.query.DeleteStageQuery
import io.sparkled.persistence.stage.impl.query.GetAllStagesQuery
import io.sparkled.persistence.stage.impl.query.GetStageByIdQuery
import io.sparkled.persistence.stage.impl.query.GetStagePropByUuidQuery
import io.sparkled.persistence.stage.impl.query.GetStagePropsByStageIdQuery
import io.sparkled.persistence.stage.impl.query.SaveStagePropsQuery
import io.sparkled.persistence.stage.impl.query.SaveStageQuery
import java.util.UUID
import javax.inject.Inject

class StagePersistenceServiceImpl
@Inject constructor(private val queryFactory: QueryFactory) : StagePersistenceService {

    override fun createStage(stage: Stage): Stage {
        return SaveStageQuery(stage).perform(queryFactory)
    }

    override fun getAllStages(): List<Stage> {
        return GetAllStagesQuery().perform(queryFactory)
    }

    override fun getStageById(stageId: Int): Stage? {
        return GetStageByIdQuery(stageId).perform(queryFactory)
    }

    override fun getStagePropsByStageId(stageId: Int): List<StageProp> {
        return GetStagePropsByStageIdQuery(stageId).perform(queryFactory)
    }

    override fun getStagePropByUuid(stageId: Int, uuid: UUID): StageProp? {
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
