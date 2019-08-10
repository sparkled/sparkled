package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.QSequence.Companion.sequence
import io.sparkled.model.entity.Sequence
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetSequenceByIdQuery(private val sequenceId: Int) : PersistenceQuery<Sequence?> {

    override fun perform(queryFactory: QueryFactory): Sequence? {
        return queryFactory
            .selectFrom(sequence)
            .where(sequence.id.eq(sequenceId))
            .fetchFirst()
    }
}
