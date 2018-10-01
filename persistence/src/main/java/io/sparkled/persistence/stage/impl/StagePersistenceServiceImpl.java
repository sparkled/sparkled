package io.sparkled.persistence.stage.impl;

import com.google.inject.persist.Transactional;
import io.sparkled.model.entity.Stage;
import io.sparkled.persistence.QueryFactory;
import io.sparkled.persistence.stage.StagePersistenceService;
import io.sparkled.persistence.stage.impl.query.DeleteStageByIdQuery;
import io.sparkled.persistence.stage.impl.query.GetAllStagesQuery;
import io.sparkled.persistence.stage.impl.query.GetStageByIdQuery;
import io.sparkled.persistence.stage.impl.query.SaveStageQuery;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class StagePersistenceServiceImpl implements StagePersistenceService {

    private QueryFactory queryFactory;

    @Inject
    public StagePersistenceServiceImpl(QueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    @Transactional
    public List<Stage> getAllStages() {
        return new GetAllStagesQuery().perform(queryFactory);
    }

    @Override
    @Transactional
    public Integer deleteStage(int stageId) {
        return new DeleteStageByIdQuery(stageId).perform(queryFactory);
    }

    @Override
    @Transactional
    public Optional<Stage> getStageById(int stageId) {
        return new GetStageByIdQuery(stageId).perform(queryFactory);
    }

    @Override
    @Transactional
    public Integer saveStage(Stage stage) {
        return new SaveStageQuery(stage).perform(queryFactory);
    }
}
