package io.sparkled.persistence.stage.impl.query;

import io.sparkled.model.entity.Stage;
import io.sparkled.model.entity.StageProp;
import io.sparkled.model.entity.StageProp_;
import io.sparkled.model.validator.StageValidator;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
    public Integer perform(EntityManager entityManager) {
        new StageValidator(stage).validate();

        Stage result = entityManager.merge(stage);
        removeDeletedStageProps(entityManager, result);
        return result.getId();
    }

    private void removeDeletedStageProps(EntityManager entityManager, Stage persistedStage) {
        List<UUID> propUuids = persistedStage.getStageProps().stream()
                .map(StageProp::getUuid)
                .collect(toList());

        if (propUuids.isEmpty()) {
            propUuids = Collections.singletonList(new UUID(0, 0)); // Need an item in the list to avoid an empty IN clause.
        }

        String className = StageProp.class.getSimpleName();
        String stageId = StageProp_.stageId.getName();
        String uuid = StageProp_.uuid.getName();
        Query query = entityManager.createQuery(
                "delete from " + className + " where " + stageId + " = :stageId and " + uuid + " not in (:propUuids)"
        );
        query.setParameter("stageId", persistedStage.getId());
        query.setParameter("propUuids", propUuids);

        int deleted = query.executeUpdate();
        logger.info("Deleted " + deleted + " " + className + " record(s) for stage " + persistedStage.getId());
    }
}
