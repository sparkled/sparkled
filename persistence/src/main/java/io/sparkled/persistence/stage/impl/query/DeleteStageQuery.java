package io.sparkled.persistence.stage.impl.query;

import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;
import io.sparkled.persistence.sequence.impl.query.DeleteSequencesQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class DeleteStageQuery implements PersistenceQuery<Void> {

    private static final Logger logger = LoggerFactory.getLogger(DeleteStageQuery.class);

    private final int stageId;

    public DeleteStageQuery(int stageId) {
        this.stageId = stageId;
    }

    @Override
    public Void perform(QueryFactory queryFactory) {
        deleteSequences(queryFactory);
        deleteStageProps(queryFactory);
        deleteStage(queryFactory);
        return null;
    }

    private void deleteSequences(QueryFactory queryFactory) {
        List<Integer> sequenceIds = queryFactory
                .select(qSequence.id)
                .from(qSequence)
                .where(qSequence.stageId.eq(stageId))
                .fetch();
        new DeleteSequencesQuery(sequenceIds).perform(queryFactory);
    }

    private void deleteStageProps(QueryFactory queryFactory) {
        List<UUID> stagePropUuids = queryFactory
                .select(qStageProp.uuid)
                .from(qStageProp)
                .where(qStageProp.stageId.eq(stageId))
                .fetch();
        new DeleteStagePropsQuery(stagePropUuids).perform(queryFactory);
    }

    private void deleteStage(QueryFactory queryFactory) {
        long deleted = queryFactory
                .delete(qStage)
                .where(qStage.id.eq(stageId))
                .execute();

        logger.info("Deleted {} stage(s).", deleted);
    }
}
