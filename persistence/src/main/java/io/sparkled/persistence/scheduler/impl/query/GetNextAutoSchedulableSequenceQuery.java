package io.sparkled.persistence.scheduler.impl.query;

import io.sparkled.model.entity.Sequence;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.Optional;

public class GetNextAutoSchedulableSequenceQuery implements PersistenceQuery<Optional<Sequence>> {

    private int previousSequenceId;

    public GetNextAutoSchedulableSequenceQuery(int previousSequenceId) {
        this.previousSequenceId = previousSequenceId;
    }

    @Override
    public Optional<Sequence> perform(QueryFactory queryFactory) {
        Sequence sequence = queryFactory
                .selectFrom(qSequence)
                .where(qSequence.autoSchedulable.eq(true).and(qSequence.id.gt(previousSequenceId)))
                .orderBy(qSequence.id.asc())
                .fetchFirst();

        return Optional.ofNullable(sequence);
    }
}
