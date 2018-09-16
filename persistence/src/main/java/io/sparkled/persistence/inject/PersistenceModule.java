package io.sparkled.persistence.inject;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;
import io.sparkled.persistence.QueryFactory;
import io.sparkled.persistence.scheduler.ScheduledSequencePersistenceService;
import io.sparkled.persistence.scheduler.impl.ScheduledSequencePersistenceServiceImpl;
import io.sparkled.persistence.sequence.SequencePersistenceService;
import io.sparkled.persistence.sequence.impl.SequencePersistenceServiceImpl;
import io.sparkled.persistence.stage.StagePersistenceService;
import io.sparkled.persistence.stage.impl.StagePersistenceServiceImpl;

public class PersistenceModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new JpaPersistModule("sparkled"));

        bind(SequencePersistenceService.class).to(SequencePersistenceServiceImpl.class).asEagerSingleton();
        bind(ScheduledSequencePersistenceService.class).to(ScheduledSequencePersistenceServiceImpl.class).asEagerSingleton();
        bind(StagePersistenceService.class).to(StagePersistenceServiceImpl.class).asEagerSingleton();
        bind(QueryFactory.class).asEagerSingleton();
    }
}
