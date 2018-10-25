package io.sparkled.persistence.stage

import io.sparkled.model.entity.Stage
import io.sparkled.model.entity.StageProp
import java.util.Optional
import java.util.UUID

interface StagePersistenceService {

    fun createStage(stage: Stage): Stage

    val allStages: List<Stage>

    fun getStageById(stageId: Int): Optional<Stage>

    fun getStagePropsByStageId(stageId: Int): List<StageProp>

    fun getStagePropByUuid(stageId: Int, uuid: UUID): Optional<StageProp>

    fun saveStage(stage: Stage, stageProps: List<StageProp>)

    fun deleteStage(stageId: Int)
}
