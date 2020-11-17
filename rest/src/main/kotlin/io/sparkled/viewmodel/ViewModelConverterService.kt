package io.sparkled.viewmodel

import io.sparkled.viewmodel.playlist.PlaylistViewModelConverter
import io.sparkled.viewmodel.playlist.search.PlaylistSearchViewModelConverter
import io.sparkled.viewmodel.playlist.sequence.PlaylistSequenceViewModelConverter
import io.sparkled.viewmodel.scheduledjob.ScheduledJobViewModelConverter
import io.sparkled.viewmodel.scheduledjob.search.ScheduledJobSearchViewModelConverter
import io.sparkled.viewmodel.sequence.SequenceViewModelConverter
import io.sparkled.viewmodel.sequence.channel.SequenceChannelViewModelConverter
import io.sparkled.viewmodel.sequence.search.SequenceSearchViewModelConverter
import io.sparkled.viewmodel.song.SongViewModelConverter
import io.sparkled.viewmodel.stage.StageViewModelConverter
import io.sparkled.viewmodel.stage.prop.StagePropViewModelConverter
import io.sparkled.viewmodel.stage.search.StageSearchViewModelConverter

interface ViewModelConverterService {
    val playlist: PlaylistViewModelConverter
    val playlistSearch: PlaylistSearchViewModelConverter
    val playlistSequence: PlaylistSequenceViewModelConverter
    val scheduledJob: ScheduledJobViewModelConverter
    val scheduledJobSearch: ScheduledJobSearchViewModelConverter
    val sequenceChannel: SequenceChannelViewModelConverter
    val sequenceSearch: SequenceSearchViewModelConverter
    val sequence: SequenceViewModelConverter
    val song: SongViewModelConverter
    val stageProp: StagePropViewModelConverter
    val stageSearch: StageSearchViewModelConverter
    val stage: StageViewModelConverter
}
