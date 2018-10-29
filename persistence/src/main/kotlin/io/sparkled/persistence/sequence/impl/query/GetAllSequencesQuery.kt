package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.QSequence.sequence
import io.sparkled.model.entity.Sequence
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetAllSequencesQuery : PersistenceQuery<List<Sequence>> {

    override fun perform(queryFactory: QueryFactory): List<Sequence> {
        return queryFactory
            .selectFrom(sequence)
            .orderBy(sequence.name.asc())
            .fetch()
    }
}
