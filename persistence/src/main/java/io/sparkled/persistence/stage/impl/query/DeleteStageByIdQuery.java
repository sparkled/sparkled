package io.sparkled.persistence.stage.impl.query;

import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;
import io.sparkled.persistence.sequence.impl.query.DeleteSequenceByIdQuery;
import io.sparkled.persistence.sequence.impl.query.GetSequencesByStageIdQuery;

public class DeleteStageByIdQuery implements PersistenceQuery<Void> {

    private final int stageId;

    public DeleteStageByIdQuery(int stageId) {
        this.stageId = stageId;
    }

    @Override
    public Void perform(QueryFactory queryFactory) {
        removeSequences(queryFactory);
        removeStageProps(queryFactory);
        removeStage(queryFactory);
        return null;
    }

    private void removeSequences(QueryFactory queryFactory) {
        new GetSequencesByStageIdQuery(stageId).perform(queryFactory).forEach(sequence -> {
            new DeleteSequenceByIdQuery(sequence.getId()).perform(queryFactory);
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
