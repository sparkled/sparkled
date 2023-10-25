package io.sparkled.viewmodel

import io.sparkled.model.enumeration.ScheduledActionType
import io.sparkled.model.entity.v2.ScheduledTaskEntity
import io.sparkled.model.util.IdUtils

data class ScheduledTaskSummaryViewModel(
    val id: Int = IdUtils.NO_ID,
    val action: ScheduledActionType,
    val cronExpression: String,
    val playlistName: String? = null,
    val playlistId: Int? = null
) {
    companion object {
        fun fromModel(model: ScheduledTaskEntity, playlistNames: Map<Int, String>): ScheduledTaskSummaryViewModel {
            return ScheduledTaskSummaryViewModel(
                id = model.id,
                action = model.action,
                cronExpression = model.cronExpression,
                playlistId = model.playlistId,
                playlistName = playlistNames[model.playlistId]
            )
        }
    }
}
