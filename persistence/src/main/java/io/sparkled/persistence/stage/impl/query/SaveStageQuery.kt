package io.sparkled.persistence.stage.impl.query;

import io.sparkled.model.entity.Stage;
import io.sparkled.model.validator.StageValidator;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

public class SaveStageQuery implements PersistenceQuery<Stage> {

    private static final Logger logger = LoggerFactory.getLogger(SaveStageQuery.class);

    private final Stage stage;

    public SaveStageQuery(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Stage perform(QueryFactory queryFactory) {
        new StageValidator().validate(stage);

        EntityManager entityManager = queryFactory.getEntityManager();
        Stage savedStage = entityManager.merge(stage);

        logger.info("Saved stage {} ({}).", savedStage.getId(), savedStage.getName());
        return savedStage;
    }
}
