package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.Stage;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.Optional;

public class GetStageBySequenceIdQuery implements PersistenceQuery<Optional<Stage>> {

    private final int sequenceId;

    public GetStageBySequenceIdQuery(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public Optional<Stage> perform(QueryFactory queryFactory) {
        Stage stage = queryFactory
                .selectFrom(qStage)
                .innerJoin(qSequence).on(qSequence.stageId.eq(qStage.id))
                .where(qSequence.id.eq(sequenceId))
                .fetchFirst();

        return Optional.ofNullable(stage);
    }
}
