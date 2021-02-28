package io.sparkled.model.entity.v2

import io.sparkled.model.annotation.Entity
import io.sparkled.model.util.IdUtils

@Entity(name = "stage", idField = "id")
data class StageEntity(
    val id: Int = IdUtils.NO_ID,
    val name: String,
    val width: Int,
    val height: Int,
) : SparkledEntity
