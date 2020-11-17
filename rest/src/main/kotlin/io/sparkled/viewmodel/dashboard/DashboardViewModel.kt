package io.sparkled.viewmodel.dashboard

import io.sparkled.viewmodel.playlist.search.PlaylistSearchViewModel
import io.sparkled.viewmodel.scheduledjob.search.ScheduledJobSearchViewModel
import io.sparkled.viewmodel.sequence.search.SequenceSearchViewModel
import io.sparkled.viewmodel.song.SongViewModel
import io.sparkled.viewmodel.stage.search.StageSearchViewModel

data class DashboardViewModel(
    val stages: List<StageSearchViewModel>,
    val songs: List<SongViewModel>,
    val sequences: List<SequenceSearchViewModel>,
    val playlists: List<PlaylistSearchViewModel>,
    val scheduledTasks: List<ScheduledJobSearchViewModel>,
)