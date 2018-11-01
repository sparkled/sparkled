package io.sparkled.persistence.stage.impl.query

import io.sparkled.model.entity.QStage.stage
import io.sparkled.model.entity.Stage
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetStageByIdQuery(private val stageId: Int) : PersistenceQuery<Stage?> {

    override fun perform(queryFactory: QueryFactory): Stage? {
        return queryFactory
            .selectFrom(stage)
            .where(stage.id.eq(stageId))
            .fetchFirst()
    }
}
