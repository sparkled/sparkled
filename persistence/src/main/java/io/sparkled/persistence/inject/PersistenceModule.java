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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersistenceModule extends AbstractModule {

    private static final Logger logger = LoggerFactory.getLogger(PersistenceModule.class);

    @Override
    protected void configure() {
        logger.info("Configuring Guice module.");

        install(new JpaPersistModule("sparkled"));
        bind(QueryFactory.class).asEagerSingleton();

        bind(StagePersistenceService.class).to(StagePersistenceServiceImpl.class).asEagerSingleton();
        bind(SongPersistenceService.class).to(SongPersistenceServiceImpl.class).asEagerSingleton();
        bind(SequencePersistenceService.class).to(SequencePersistenceServiceImpl.class).asEagerSingleton();
        bind(PlaylistPersistenceService.class).to(PlaylistPersistenceServiceImpl.class).asEagerSingleton();
    }
}
