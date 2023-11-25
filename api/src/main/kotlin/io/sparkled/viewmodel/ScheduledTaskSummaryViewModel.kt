package io.sparkled.viewmodel

import io.sparkled.model.ScheduledActionModel
import io.sparkled.model.UniqueId
import io.sparkled.model.annotation.GenerateClientType
import io.sparkled.model.enumeration.ScheduledActionType

@GenerateClientType
data class ScheduledTaskSummaryViewModel(
    val id: UniqueId,
    val type: ScheduledActionType,
    val cronExpression: String,
    val playlistName: String? = null,
    val playlistId: UniqueId? = null,
) : ViewModel {
    companion object {
        fun fromModel(
            model: ScheduledActionModel,
            playlistNames: Map<UniqueId, String>
        ): ScheduledTaskSummaryViewModel {
            return ScheduledTaskSummaryViewModel(
                id = model.id,
                type = model.type,
                cronExpression = model.cronExpression,
                playlistId = model.playlistId,
                playlistName = playlistNames[model.playlistId],
            )
        }
    }
}
