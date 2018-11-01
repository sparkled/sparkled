package io.sparkled.viewmodel.scheduledjob

import io.sparkled.model.entity.ScheduledJobAction
import io.sparkled.viewmodel.ViewModel

data class ScheduledJobViewModel(
    var id: Int?,
    var action: ScheduledJobAction?,
    var cronExpression: String?,
    var value: String?,
    var playlistId: Int?
) : ViewModel
