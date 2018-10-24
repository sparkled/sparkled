package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.Sequence;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.List;

public class GetSequencesByStageIdQuery implements PersistenceQuery<List<Sequence>> {

    private final int stageId;

    public GetSequencesByStageIdQuery(int stageId) {
        this.stageId = stageId;
    }

    @Override
    public List<Sequence> perform(QueryFactory queryFactory) {
        return queryFactory
                .selectFrom(qSequence)
                .where(qSequence.stageId.eq(stageId))
                .orderBy(qSequence.id.asc())
                .fetch();
    }
}
