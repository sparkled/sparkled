package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.Sequence;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.List;

public class GetAllSequencesQuery implements PersistenceQuery<List<Sequence>> {

    @Override
    public List<Sequence> perform(QueryFactory queryFactory) {
        return queryFactory
                .selectFrom(qSequence)
                .orderBy(qSequence.name.asc())
                .fetch();
    }
}
