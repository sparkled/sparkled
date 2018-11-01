package io.sparkled.persistence.stage

import io.sparkled.model.entity.Stage
import io.sparkled.model.entity.StageProp
import java.util.UUID

interface StagePersistenceService {

    fun createStage(stage: Stage): Stage

    fun getAllStages(): List<Stage>

    fun getStageById(stageId: Int): Stage?

    fun getStagePropsByStageId(stageId: Int): List<StageProp>

    fun getStagePropByUuid(stageId: Int, uuid: UUID): StageProp?

    fun saveStage(stage: Stage, stageProps: List<StageProp>)

    fun deleteStage(stageId: Int)
}
