package net.chrisparton.sparkled.persistence.inject;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;
import net.chrisparton.sparkled.persistence.scheduler.ScheduledSongPersistenceService;
import net.chrisparton.sparkled.persistence.scheduler.ScheduledSongPersistenceServiceImpl;
import net.chrisparton.sparkled.persistence.song.SongPersistenceService;
import net.chrisparton.sparkled.persistence.song.SongPersistenceServiceImpl;
import net.chrisparton.sparkled.persistence.stage.StagePersistenceService;
import net.chrisparton.sparkled.persistence.stage.StagePersistenceServiceImpl;

public class PersistenceModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new JpaPersistModule("sparkled"));

        bind(SongPersistenceService.class).to(SongPersistenceServiceImpl.class).asEagerSingleton();
        bind(ScheduledSongPersistenceService.class).to(ScheduledSongPersistenceServiceImpl.class).asEagerSingleton();
        bind(StagePersistenceService.class).to(StagePersistenceServiceImpl.class).asEagerSingleton();
    }
}
