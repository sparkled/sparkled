package io.sparkled.persistence.inject;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;
import io.sparkled.persistence.scheduler.impl.ScheduledSongPersistenceServiceImpl;
import io.sparkled.persistence.song.impl.SongPersistenceServiceImpl;
import io.sparkled.persistence.stage.StagePersistenceService;
import io.sparkled.persistence.stage.impl.StagePersistenceServiceImpl;
import io.sparkled.persistence.scheduler.ScheduledSongPersistenceService;
import io.sparkled.persistence.song.SongPersistenceService;

public class PersistenceModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new JpaPersistModule("sparkled"));

        bind(SongPersistenceService.class).to(SongPersistenceServiceImpl.class).asEagerSingleton();
        bind(ScheduledSongPersistenceService.class).to(ScheduledSongPersistenceServiceImpl.class).asEagerSingleton();
        bind(StagePersistenceService.class).to(StagePersistenceServiceImpl.class).asEagerSingleton();
    }
}
