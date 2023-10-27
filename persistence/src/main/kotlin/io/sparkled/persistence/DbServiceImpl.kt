package io.sparkled.persistence

import io.sparkled.persistence.repository.PlaylistRepository
import io.sparkled.persistence.repository.PlaylistSequenceRepository
import io.sparkled.persistence.repository.ScheduledActionRepository
import io.sparkled.persistence.repository.SequenceChannelRepository
import io.sparkled.persistence.repository.SequenceRepository
import io.sparkled.persistence.repository.SettingRepository
import io.sparkled.persistence.repository.SongRepository
import io.sparkled.persistence.repository.StagePropRepository
import io.sparkled.persistence.repository.StageRepository
import jakarta.inject.Singleton

@Singleton
class DbServiceImpl(
    override val playlists: PlaylistRepository,
    override val playlistSequences: PlaylistSequenceRepository,
    override val scheduledActions: ScheduledActionRepository,
    override val sequences: SequenceRepository,
    override val sequenceChannels: SequenceChannelRepository,
    override val settings: SettingRepository,
    override val songs: SongRepository,
    override val stages: StageRepository,
    override val stageProps: StagePropRepository,
) : DbService