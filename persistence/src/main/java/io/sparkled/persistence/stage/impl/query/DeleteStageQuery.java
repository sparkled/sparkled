package io.sparkled.persistence.stage.impl.query;

import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;
import io.sparkled.persistence.sequence.impl.query.DeleteSequenceQuery;
import io.sparkled.persistence.sequence.impl.query.GetSequencesByStageIdQuery;

public class DeleteStageQuery implements PersistenceQuery<Integer> {

    private final int stageId;

    public DeleteStageQuery(int stageId) {
        this.stageId = stageId;
    }

    @Override
    public Integer perform(QueryFactory queryFactory) {
        removeSequences(queryFactory);
        removeStageProps(queryFactory);
        removeStage(queryFactory);

        return stageId;
    }

    private void removeSequences(QueryFactory queryFactory) {
        new GetSequencesByStageIdQuery(stageId).perform(queryFactory).forEach(sequence -> {
            new DeleteSequenceQuery(sequence.getId()).perform(queryFactory);
        });
    }

    private void removeStageProps(QueryFactory queryFactory) {
        queryFactory
                .delete(qStageProp)
                .where(qStageProp.stageId.eq(stageId))
                .execute();
    }

    private void removeStage(QueryFactory queryFactory) {
        queryFactory
                .delete(qStage)
                .where(qStage.id.eq(stageId))
                .execute();
    }
}
