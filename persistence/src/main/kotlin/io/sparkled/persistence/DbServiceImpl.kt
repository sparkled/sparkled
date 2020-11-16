package io.sparkled.persistence

import io.sparkled.persistence.playlist.PlaylistPersistenceService
import io.sparkled.persistence.scheduledjob.ScheduledJobPersistenceService
import io.sparkled.persistence.sequence.SequencePersistenceService
import io.sparkled.persistence.setting.SettingPersistenceService
import io.sparkled.persistence.song.SongPersistenceService
import io.sparkled.persistence.stage.StagePersistenceService
import javax.inject.Singleton

@Singleton
class DbServiceImpl(
    override val playlist: PlaylistPersistenceService,
    override val scheduledJob: ScheduledJobPersistenceService,
    override val sequence: SequencePersistenceService,
    override val setting: SettingPersistenceService,
    override val song: SongPersistenceService,
    override val stage: StagePersistenceService
) : DbService
