package io.sparkled.persistence.sequence.impl.query

import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.PersistenceQuery.Companion.qSequence
import io.sparkled.persistence.PersistenceQuery.Companion.qStage
import io.sparkled.persistence.PersistenceQuery.Companion.qStageProp
import io.sparkled.persistence.QueryFactory
import java.util.*

class GetSequenceStagePropUuidMapBySequenceIdQuery(private val sequenceId: Int) : PersistenceQuery<Map<String, UUID>> {

    override fun perform(queryFactory: QueryFactory): Map<String, UUID> {
        val stageProps = queryFactory
                .select(qStageProp)
                .from(qSequence)
                .innerJoin(qStage).on(qSequence.stageId.eq(qStage.id))
                .innerJoin(qStageProp).on(qStage.id.eq(qStageProp.stageId))
                .where(qSequence.id.eq(sequenceId))
                .fetch()

        return stageProps.associateBy({ s -> s.getCode()!! }, { s -> s.getUuid()!! })
    }
}
