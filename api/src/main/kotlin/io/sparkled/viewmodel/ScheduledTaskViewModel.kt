package io.sparkled.viewmodel

import io.sparkled.model.enumeration.ScheduledActionType
import io.sparkled.model.entity.v2.ScheduledTaskEntity
import io.sparkled.model.util.IdUtils

data class ScheduledTaskViewModel(
    var id: Int = IdUtils.NO_ID,
    var action: ScheduledActionType,
    var cronExpression: String,
    var value: String? = null,
    var playlistId: Int? = null
) {
    fun toModel() = ScheduledTaskEntity(
        id = id,
        action = action,
        cronExpression = cronExpression,
        value = value,
        playlistId = playlistId
    )

    companion object {
        fun fromModel(model: ScheduledTaskEntity) = ScheduledTaskViewModel(
            id = model.id,
            action = model.action,
            cronExpression = model.cronExpression,
            value = model.value,
            playlistId = model.playlistId
        )
    }
}
