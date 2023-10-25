package io.sparkled.viewmodel

import io.sparkled.model.ScheduledActionModel
import io.sparkled.model.UniqueId
import io.sparkled.model.annotation.GenerateClientType
import io.sparkled.model.enumeration.ScheduledActionType

@GenerateClientType
data class ScheduledActionViewModel(
    var id: UniqueId,
    var type: ScheduledActionType,
    var cronExpression: String,
    var value: String? = null,
    var playlistId: UniqueId? = null
) : ViewModel {
    fun toModel() = ScheduledActionModel(
        id = id,
        type = type,
        cronExpression = cronExpression,
        value = value,
        playlistId = playlistId,
    )

    companion object {
        fun fromModel(model: ScheduledActionModel) = ScheduledActionViewModel(
            id = model.id,
            type = model.type,
            cronExpression = model.cronExpression,
            value = model.value,
            playlistId = model.playlistId,
        )
    }
}
