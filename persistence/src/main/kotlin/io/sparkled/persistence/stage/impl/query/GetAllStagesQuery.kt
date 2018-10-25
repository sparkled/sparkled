package io.sparkled.persistence.stage.impl.query;

import io.sparkled.model.entity.Stage;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.List;

public class GetAllStagesQuery implements PersistenceQuery<List<Stage>> {

    @Override
    public List<Stage> perform(QueryFactory queryFactory) {
        return queryFactory
                .selectFrom(qStage)
                .orderBy(qStage.name.asc())
                .fetch();
    }
}
