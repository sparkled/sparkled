package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.Sequence;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.Optional;

public class GetSequenceByIdQuery implements PersistenceQuery<Optional<Sequence>> {

    private final int sequenceId;

    public GetSequenceByIdQuery(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public Optional<Sequence> perform(QueryFactory queryFactory) {
        Sequence sequence = queryFactory
                .selectFrom(qSequence)
                .where(qSequence.id.eq(sequenceId))
                .fetchFirst();

        return Optional.ofNullable(sequence);
    }
}
