package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.Sequence
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.PersistenceQuery.Companion.qSequence
import io.sparkled.persistence.QueryFactory
import java.util.Optional

class GetSequenceByIdQuery(private val sequenceId: Int) : PersistenceQuery<Optional<Sequence>> {

    override fun perform(queryFactory: QueryFactory): Optional<Sequence> {
        val sequence = queryFactory
                .selectFrom(qSequence)
                .where(qSequence.id.eq(sequenceId))
                .fetchFirst()

        return Optional.ofNullable(sequence)
    }
}
