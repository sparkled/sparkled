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
import javax.inject.Singleton

@Singleton
class ViewModelConverterServiceImpl(
    override val playlist: PlaylistViewModelConverter,
    override val playlistSearch: PlaylistSearchViewModelConverter,
    override val playlistSequence: PlaylistSequenceViewModelConverter,
    override val scheduledJob: ScheduledJobViewModelConverter,
    override val scheduledJobSearch: ScheduledJobSearchViewModelConverter,
    override val sequenceChannel: SequenceChannelViewModelConverter,
    override val sequenceSearch: SequenceSearchViewModelConverter,
    override val sequence: SequenceViewModelConverter,
    override val song: SongViewModelConverter,
    override val stageProp: StagePropViewModelConverter,
    override val stageSearch: StageSearchViewModelConverter,
    override val stage: StageViewModelConverter
) : ViewModelConverterService
