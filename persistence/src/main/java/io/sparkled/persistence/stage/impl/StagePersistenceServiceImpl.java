package io.sparkled.persistence.stage.impl;

import com.google.inject.persist.Transactional;
import io.sparkled.model.entity.Stage;
import io.sparkled.persistence.stage.StagePersistenceService;
import io.sparkled.persistence.stage.impl.query.DeleteStageQuery;
import io.sparkled.persistence.stage.impl.query.GetAllStagesQuery;
import io.sparkled.persistence.stage.impl.query.GetStageByIdQuery;
import io.sparkled.persistence.stage.impl.query.SaveStageQuery;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class StagePersistenceServiceImpl implements StagePersistenceService {

    private Provider<EntityManager> entityManagerProvider;

    @Inject
    public StagePersistenceServiceImpl(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    @Override
    @Transactional
    public List<Stage> getAllStages() {
        return new GetAllStagesQuery().perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Integer deleteStage(int stageId) {
        return new DeleteStageQuery(stageId).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Optional<Stage> getStageById(int stageId) {
        return new GetStageByIdQuery(stageId).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Integer saveStage(Stage stage) {
        return new SaveStageQuery(stage).perform(entityManagerProvider.get());
    }
}
