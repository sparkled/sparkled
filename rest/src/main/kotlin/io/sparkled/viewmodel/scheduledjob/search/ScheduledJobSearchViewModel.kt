package io.sparkled.viewmodel.scheduledjob.search

import io.sparkled.model.entity.ScheduledJobAction
import io.sparkled.viewmodel.ViewModel

data class ScheduledJobSearchViewModel(
    val id: Int?,
    val action: ScheduledJobAction?,
    val cronExpression: String?,
    val playlistName: String?,
    val playlistId: Int?
) : ViewModel
