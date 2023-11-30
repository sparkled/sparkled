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

interface DbService {
    val playlistSequences: PlaylistSequenceRepository
    val playlists: PlaylistRepository
    val scheduledActions: ScheduledActionRepository
    val sequenceChannels: SequenceChannelRepository
    val sequences: SequenceRepository
    val settings: SettingRepository
    val songs: SongRepository
    val stageProps: StagePropRepository
    val stages: StageRepository

    // TODO workaround for Micronaut Data error: "Could not set JDBC Connection read-only: Cannot change read-only flag after establishing a connection."
    fun <T> inTransaction(fn: () -> T): T
}
