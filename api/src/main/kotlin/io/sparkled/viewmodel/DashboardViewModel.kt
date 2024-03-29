package io.sparkled.viewmodel

import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
data class DashboardViewModel(
    val playlists: List<PlaylistSummaryViewModel>,
    val scheduledTasks: List<ScheduledTaskSummaryViewModel>,
    val sequences: List<SequenceSummaryViewModel>,
    val songs: List<SongViewModel>,
    val stages: List<StageSummaryViewModel>,
) : ViewModel
