package io.sparkled.persistence.stage.impl.query;

import io.sparkled.model.entity.Stage;
import io.sparkled.model.entity.StageProp;
import io.sparkled.model.validator.StageValidator;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toList;

public class SaveStageQuery implements PersistenceQuery<Integer> {

    private static final Logger logger = Logger.getLogger(SaveStageQuery.class.getName());

    private final Stage stage;

    public SaveStageQuery(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Integer perform(QueryFactory queryFactory) {
        EntityManager entityManager = queryFactory.getEntityManager();

        new StageValidator(stage).validate();

        Stage result = entityManager.merge(stage);
        removeDeletedStageProps(queryFactory, result);
        return result.getId();
    }

    private void removeDeletedStageProps(QueryFactory queryFactory, Stage persistedStage) {
        List<UUID> propUuids = persistedStage.getStageProps().stream()
                .map(StageProp::getUuid)
                .collect(toList());

        if (propUuids.isEmpty()) {
            propUuids = Collections.singletonList(new UUID(0, 0)); // Need an item in the list to avoid an empty IN clause.
        }

        long deleted = queryFactory
                .delete(qStageProp)
                .where(qStageProp.stageId.eq(persistedStage.getId()).and(qStageProp.uuid.notIn(propUuids)))
                .execute();

        logger.info("Deleted " + deleted + " record(s) for stage " + persistedStage.getId());
    }
}
