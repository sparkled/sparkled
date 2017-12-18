package io.sparkled.persistence.stage.impl;

import com.google.inject.persist.Transactional;
import io.sparkled.model.entity.Stage;
import io.sparkled.persistence.stage.StagePersistenceService;
import io.sparkled.persistence.stage.impl.query.GetStageByIdQuery;

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
