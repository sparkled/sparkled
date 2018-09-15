package io.sparkled.persistence.stage.impl.query;

import io.sparkled.model.entity.Stage;
import io.sparkled.model.entity.StageProp;
import io.sparkled.model.entity.StageProp_;
import io.sparkled.model.entity.Stage_;
import io.sparkled.persistence.BulkDeleteQuery;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.sequence.impl.query.DeleteSequenceQuery;
import io.sparkled.persistence.sequence.impl.query.GetSequencesByStageIdQuery;

import javax.persistence.EntityManager;

public class DeleteStageQuery implements PersistenceQuery<Integer> {

    private final int stageId;

    public DeleteStageQuery(int stageId) {
        this.stageId = stageId;
    }

    @Override
    public Integer perform(EntityManager entityManager) {
        removeSequences(entityManager);
        removeStageProps(entityManager);
        removeStage(entityManager);

        return stageId;
    }

    private void removeSequences(EntityManager entityManager) {
        new GetSequencesByStageIdQuery(stageId).perform(entityManager).forEach(sequence -> {
            new DeleteSequenceQuery(sequence.getId()).perform(entityManager);
        });
    }

    private void removeStageProps(EntityManager entityManager) {
        BulkDeleteQuery.from(StageProp.class).where(StageProp_.stageId).is(stageId).perform(entityManager);
    }

    private void removeStage(EntityManager entityManager) {
        BulkDeleteQuery.from(Stage.class).where(Stage_.id).is(stageId).perform(entityManager);
    }
}
