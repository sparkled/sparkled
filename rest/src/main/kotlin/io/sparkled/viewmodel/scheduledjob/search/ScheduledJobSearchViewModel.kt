package io.sparkled.viewmodel.scheduledjob.search

import io.sparkled.model.entity.ScheduledJobAction
import io.sparkled.viewmodel.ViewModel

data class ScheduledJobSearchViewModel(
    private var id: Int?,
    private var action: ScheduledJobAction?,
    private var cronExpression: String?,
    private var playlistName: String?,
    private var playlistId: Int?
) : ViewModel
