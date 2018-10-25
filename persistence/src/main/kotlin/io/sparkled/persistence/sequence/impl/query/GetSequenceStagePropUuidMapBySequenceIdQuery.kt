package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.StageProp
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import java.util.UUID

import java.util.stream.Collectors.toMap

class GetSequenceStagePropUuidMapBySequenceIdQuery(private val sequenceId: Int) : PersistenceQuery<Map<String, UUID>> {

    @Override
    fun perform(queryFactory: QueryFactory): Map<String, UUID> {
        val stageProps = queryFactory
                .select(qStageProp)
                .from(qSequence)
                .innerJoin(qStage).on(qSequence.stageId.eq(qStage.id))
                .innerJoin(qStageProp).on(qStage.id.eq(qStageProp.stageId))
                .where(qSequence.id.eq(sequenceId))
                .fetch()

        return stageProps.stream().collect(toMap(???({ StageProp.getCode() }), ???({ StageProp.getUuid() })))
    }
}
