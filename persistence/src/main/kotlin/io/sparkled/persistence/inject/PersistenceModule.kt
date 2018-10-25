package io.sparkled.persistence.inject

import com.google.inject.AbstractModule
import com.google.inject.persist.jpa.JpaPersistModule
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.playlist.PlaylistPersistenceService
import io.sparkled.persistence.playlist.impl.PlaylistPersistenceServiceImpl
import io.sparkled.persistence.sequence.SequencePersistenceService
import io.sparkled.persistence.sequence.impl.SequencePersistenceServiceImpl
import io.sparkled.persistence.setting.SettingPersistenceService
import io.sparkled.persistence.setting.impl.SettingPersistenceServiceImpl
import io.sparkled.persistence.song.SongPersistenceService
import io.sparkled.persistence.song.impl.SongPersistenceServiceImpl
import io.sparkled.persistence.stage.StagePersistenceService
import io.sparkled.persistence.stage.impl.StagePersistenceServiceImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PersistenceModule : AbstractModule() {

    @Override
    protected fun configure() {
        logger.info("Configuring Guice module.")

        install(JpaPersistModule("sparkled"))
        bind(QueryFactory::class.java).asEagerSingleton()

        bind(StagePersistenceService::class.java).to(StagePersistenceServiceImpl::class.java).asEagerSingleton()
        bind(SongPersistenceService::class.java).to(SongPersistenceServiceImpl::class.java).asEagerSingleton()
        bind(SequencePersistenceService::class.java).to(SequencePersistenceServiceImpl::class.java).asEagerSingleton()
        bind(PlaylistPersistenceService::class.java).to(PlaylistPersistenceServiceImpl::class.java).asEagerSingleton()
        bind(SettingPersistenceService::class.java).to(SettingPersistenceServiceImpl::class.java).asEagerSingleton()
    }

    companion object {

        private val logger = LoggerFactory.getLogger(PersistenceModule::class.java)
    }
}
