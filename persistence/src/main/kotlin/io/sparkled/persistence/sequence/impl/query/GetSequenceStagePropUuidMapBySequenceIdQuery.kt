package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.QSequence.sequence
import io.sparkled.model.entity.QStage.stage
import io.sparkled.model.entity.QStageProp.stageProp
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import java.util.UUID

class GetSequenceStagePropUuidMapBySequenceIdQuery(private val sequenceId: Int) : PersistenceQuery<Map<String, UUID>> {

    override fun perform(queryFactory: QueryFactory): Map<String, UUID> {
        val stageProps = queryFactory
            .select(stageProp)
            .from(sequence)
            .innerJoin(stage).on(sequence.stageId.eq(stage.id))
            .innerJoin(stageProp).on(stage.id.eq(stageProp.stageId))
            .where(sequence.id.eq(sequenceId))
            .fetch()

        return stageProps.associateBy({ s -> s.getCode()!! }, { s -> s.getUuid()!! })
    }
}
