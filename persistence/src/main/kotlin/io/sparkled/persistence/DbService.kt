package io.sparkled.persistence

import io.sparkled.persistence.playlist.PlaylistPersistenceService
import io.sparkled.persistence.scheduledjob.ScheduledJobPersistenceService
import io.sparkled.persistence.sequence.SequencePersistenceService
import io.sparkled.persistence.setting.SettingPersistenceService
import io.sparkled.persistence.song.SongPersistenceService
import io.sparkled.persistence.stage.StagePersistenceService

interface DbService {
    val playlist: PlaylistPersistenceService
    val scheduledJob: ScheduledJobPersistenceService
    val sequence: SequencePersistenceService
    val setting: SettingPersistenceService
    val song: SongPersistenceService
    val stage: StagePersistenceService
}
