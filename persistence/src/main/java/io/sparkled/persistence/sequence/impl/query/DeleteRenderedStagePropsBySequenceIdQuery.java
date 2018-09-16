package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

public class DeleteRenderedStagePropsBySequenceIdQuery implements PersistenceQuery<Void> {

    private final int sequenceId;

    public DeleteRenderedStagePropsBySequenceIdQuery(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public Void perform(QueryFactory queryFactory) {
        queryFactory
                .delete(qRenderedStageProp)
                .where(qRenderedStageProp.sequenceId.eq(sequenceId))
                .execute();

        return null;
    }
}
