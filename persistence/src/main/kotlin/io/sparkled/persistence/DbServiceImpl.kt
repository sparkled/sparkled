package io.sparkled.persistence

import io.sparkled.model.SettingModel
import io.sparkled.model.util.IdUtils.uniqueId
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
import io.micronaut.transaction.annotation.Transactional
import org.jetbrains.annotations.TestOnly

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
) : DbService {

    @TestOnly
    @Transactional
    fun withTransaction(fn: () -> Unit) {
        fn.invoke()
    }

    @TestOnly
    @Transactional
    fun testTransaction(fail: Boolean) {
        settings.save(SettingModel(id = "BRIGHTNESS", value = "TEST"))

        if (fail) {
            // Attempt to insert duplicate record. Should cause a rollback, resulting in no users being persisted.
            settings.save(SettingModel(id = "BRIGHTNESS", value = "TEST"))
        }
    }

    @TestOnly
    fun deleteEverything() {
        scheduledActions.deleteAll()
        settings.deleteAll()
        playlistSequences.deleteAll()
        playlists.deleteAll()
        sequenceChannels.deleteAll()
        sequences.deleteAll()
        stageProps.deleteAll()
        stages.deleteAll()
        songs.deleteAll()
    }
}
