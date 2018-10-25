package io.sparkled.persistence.stage.impl.query;

import io.sparkled.model.entity.StageProp;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.List;

public class GetStagePropsByStageIdQuery implements PersistenceQuery<List<StageProp>> {

    private final int stageId;

    public GetStagePropsByStageIdQuery(int stageId) {
        this.stageId = stageId;
    }

    @Override
    public List<StageProp> perform(QueryFactory queryFactory) {
        return queryFactory
                .selectFrom(qStageProp)
                .where(qStageProp.stageId.eq(stageId))
                .orderBy(qStageProp.displayOrder.asc())
                .fetch();
    }
}
