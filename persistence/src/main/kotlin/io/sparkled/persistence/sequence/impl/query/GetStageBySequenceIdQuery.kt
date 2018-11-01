package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.QSequence.sequence
import io.sparkled.model.entity.QStage.stage
import io.sparkled.model.entity.Stage
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetStageBySequenceIdQuery(private val sequenceId: Int) : PersistenceQuery<Stage?> {

    override fun perform(queryFactory: QueryFactory): Stage? {
        return queryFactory
            .selectFrom(stage)
            .innerJoin(sequence).on(sequence.stageId.eq(stage.id))
            .where(sequence.id.eq(sequenceId))
            .fetchFirst()
    }
}
