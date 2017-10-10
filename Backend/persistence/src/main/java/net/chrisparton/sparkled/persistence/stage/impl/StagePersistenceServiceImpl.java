package net.chrisparton.sparkled.persistence.stage.impl;

import com.google.inject.persist.Transactional;
import net.chrisparton.sparkled.model.entity.Stage;
import net.chrisparton.sparkled.persistence.stage.StagePersistenceService;
import net.chrisparton.sparkled.persistence.stage.impl.query.GetStageByIdQuery;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import java.util.Optional;

public class StagePersistenceServiceImpl implements StagePersistenceService {

    private Provider<EntityManager> entityManagerProvider;

    @Inject
    public StagePersistenceServiceImpl(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    @Override
    @Transactional
    public Optional<Stage> getStageById(int stageId) {
        return new GetStageByIdQuery(stageId).perform(entityManagerProvider.get());
    }
}
