package io.sparkled.viewmodel

import io.sparkled.model.UniqueId
import io.sparkled.model.enumeration.ScheduledActionType

data class ScheduledActionEditViewModel(
    var type: ScheduledActionType,
    var cronExpression: String,
    var value: String? = null,
    var playlistId: UniqueId? = null
) : ViewModel
