package io.sparkled.persistence.inject;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;
import io.sparkled.persistence.QueryFactory;
import io.sparkled.persistence.playlist.PlaylistPersistenceService;
import io.sparkled.persistence.playlist.impl.PlaylistPersistenceServiceImpl;
import io.sparkled.persistence.sequence.SequencePersistenceService;
import io.sparkled.persistence.sequence.impl.SequencePersistenceServiceImpl;
import io.sparkled.persistence.song.SongPersistenceService;
import io.sparkled.persistence.song.impl.SongPersistenceServiceImpl;
import io.sparkled.persistence.stage.StagePersistenceService;
import io.sparkled.persistence.stage.impl.StagePersistenceServiceImpl;

public class PersistenceModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new JpaPersistModule("sparkled"));

        bind(SongPersistenceService.class).to(SongPersistenceServiceImpl.class).asEagerSingleton();
        bind(SequencePersistenceService.class).to(SequencePersistenceServiceImpl.class).asEagerSingleton();
        bind(PlaylistPersistenceService.class).to(PlaylistPersistenceServiceImpl.class).asEagerSingleton();
        bind(StagePersistenceService.class).to(StagePersistenceServiceImpl.class).asEagerSingleton();
        bind(QueryFactory.class).asEagerSingleton();
    }
}
