package io.sparkled.persistence.stage.impl.query;

import io.sparkled.model.entity.Stage;
import io.sparkled.model.entity.StageProp;
import io.sparkled.model.validator.StagePropValidator;
import io.sparkled.model.validator.exception.EntityValidationException;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public class SaveStagePropsQuery implements PersistenceQuery<Void> {

    private static final Logger logger = LoggerFactory.getLogger(SaveStagePropsQuery.class);

    private final Stage stage;
    private final List<StageProp> stageProps;

    public SaveStagePropsQuery(Stage stage, List<StageProp> stageProps) {
        this.stage = stage;
        this.stageProps = stageProps;
    }

    @Override
    public Void perform(QueryFactory queryFactory) {
        StagePropValidator stagePropValidator = new StagePropValidator();
        stageProps.forEach(sp -> sp.setStageId(stage.getId()));
        stageProps.forEach(stagePropValidator::validate);

        if (uuidAlreadyInUse(queryFactory)) {
            throw new EntityValidationException("Stage prop already exists on another stage.");
        } else {
            EntityManager entityManager = queryFactory.getEntityManager();
            stageProps.forEach(entityManager::merge);
            logger.info("Saved {} stage prop(s) for stage {}.", stageProps.size(), stage.getId());

            deleteRemovedStageProps(queryFactory);
            return null;
        }
    }

    private boolean uuidAlreadyInUse(QueryFactory queryFactory) {
        List<UUID> uuidsToCheck = stageProps.stream().map(StageProp::getUuid).collect(toList());
        uuidsToCheck = uuidsToCheck.isEmpty() ? noUuids : uuidsToCheck;

        long uuidsInUse = queryFactory.select(qStageProp)
                .from(qStageProp)
                .where(qStageProp.stageId.ne(stage.getId()).and(qStageProp.uuid.in(uuidsToCheck)))
                .fetchCount();
        return uuidsInUse > 0;
    }

    private void deleteRemovedStageProps(QueryFactory queryFactory) {
        List<UUID> uuidsToDelete = getStagePropUuidsToDelete(queryFactory);
        new DeleteStagePropsQuery(uuidsToDelete).perform(queryFactory);
    }

    private List<UUID> getStagePropUuidsToDelete(QueryFactory queryFactory) {
        List<UUID> uuidsToKeep = stageProps.stream().map(StageProp::getUuid).collect(toList());
        uuidsToKeep = uuidsToKeep.isEmpty() ? noUuids : uuidsToKeep;

        return queryFactory
                .select(qStageProp.uuid)
                .from(qStageProp)
                .where(qStageProp.stageId.eq(stage.getId()).and(qStageProp.uuid.notIn(uuidsToKeep)))
                .fetch();
    }
}
