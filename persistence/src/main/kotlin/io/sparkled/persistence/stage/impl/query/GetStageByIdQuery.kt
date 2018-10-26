package io.sparkled.persistence.stage.impl.query

import io.sparkled.model.entity.Stage
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.PersistenceQuery.Companion.qStage
import io.sparkled.persistence.QueryFactory
import java.util.Optional

class GetStageByIdQuery(private val stageId: Int) : PersistenceQuery<Optional<Stage>> {

    override fun perform(queryFactory: QueryFactory): Optional<Stage> {
        val stage = queryFactory
                .selectFrom(qStage)
                .where(qStage.id.eq(stageId))
                .fetchFirst()

        return Optional.ofNullable(stage)
    }
}
