package io.sparkled.model.entity.v2

import io.sparkled.model.annotation.Entity
import io.sparkled.model.util.IdUtils
import java.util.UUID

@Entity(name = "stage_prop", idField = "uuid")
data class StagePropEntity(
    val uuid: UUID = IdUtils.newUuid(),
    val stageId: Int = IdUtils.NO_ID,
    val code: String = "",
    val name: String = "",
    val type: String = "",
    val ledCount: Int = 0,
    val reverse: Boolean = false,
    val positionX: Int = 0,
    val positionY: Int = 0,
    val scaleX: Float = 1f,
    val scaleY: Float = 1f,
    val rotation: Int = 0,
    val brightness: Int = 100, // TODO move to constant
    val displayOrder: Int = 0,
    val ledPositionsJson: String = "[]",
) : SparkledEntity
