package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.QSequence.sequence
import io.sparkled.model.entity.QStage.stage
import io.sparkled.model.entity.Stage
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import java.util.Optional

class GetStageBySequenceIdQuery(private val sequenceId: Int) : PersistenceQuery<Optional<Stage>> {

    override fun perform(queryFactory: QueryFactory): Optional<Stage> {
        val stage = queryFactory
            .selectFrom(stage)
            .innerJoin(sequence).on(sequence.stageId.eq(stage.id))
            .where(sequence.id.eq(sequenceId))
            .fetchFirst()

        return Optional.ofNullable(stage)
    }
}
