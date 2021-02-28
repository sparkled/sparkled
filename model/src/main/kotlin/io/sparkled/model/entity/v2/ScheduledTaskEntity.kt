package io.sparkled.model.entity.v2

import io.sparkled.model.annotation.Entity
import io.sparkled.model.entity.ScheduledJobAction
import io.sparkled.model.util.IdUtils

// TODO rename to ScheduledAction and rename DB table.
@Entity(name = "scheduled_job", idField = "id")
data class ScheduledTaskEntity(
    val id: Int = IdUtils.NO_ID,
    val action: ScheduledJobAction,
    val cronExpression: String,
    val value: String?,
    val playlistId: Int?,
) : SparkledEntity
