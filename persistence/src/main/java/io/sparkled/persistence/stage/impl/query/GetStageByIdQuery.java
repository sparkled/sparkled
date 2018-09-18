package io.sparkled.persistence.stage.impl.query;

import io.sparkled.model.entity.Stage;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.Optional;

public class GetStageByIdQuery implements PersistenceQuery<Optional<Stage>> {

    private final int stageId;

    public GetStageByIdQuery(int stageId) {
        this.stageId = stageId;
    }

    @Override
    public Optional<Stage> perform(QueryFactory queryFactory) {
        final Stage stage = queryFactory
                .selectFrom(qStage)
                .where(qStage.id.eq(stageId))
                .fetchFirst();

        return Optional.ofNullable(stage);
    }
}
