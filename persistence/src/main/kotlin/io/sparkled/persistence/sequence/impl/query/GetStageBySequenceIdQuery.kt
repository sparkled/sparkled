package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.Stage
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.PersistenceQuery.Companion.qSequence
import io.sparkled.persistence.PersistenceQuery.Companion.qStage
import io.sparkled.persistence.QueryFactory
import java.util.*

class GetStageBySequenceIdQuery(private val sequenceId: Int) : PersistenceQuery<Optional<Stage>> {

    override fun perform(queryFactory: QueryFactory): Optional<Stage> {
        val stage = queryFactory
                .selectFrom(qStage)
                .innerJoin(qSequence).on(qSequence.stageId.eq(qStage.id))
                .where(qSequence.id.eq(sequenceId))
                .fetchFirst()

        return Optional.ofNullable(stage)
    }
}
