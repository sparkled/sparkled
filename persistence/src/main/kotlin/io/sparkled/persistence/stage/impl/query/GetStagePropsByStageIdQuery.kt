package io.sparkled.persistence.stage.impl.query

import io.sparkled.model.entity.StageProp
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.PersistenceQuery.Companion.qStageProp
import io.sparkled.persistence.QueryFactory

class GetStagePropsByStageIdQuery(private val stageId: Int) : PersistenceQuery<List<StageProp>> {

    override fun perform(queryFactory: QueryFactory): List<StageProp> {
        return queryFactory
                .selectFrom(qStageProp)
                .where(qStageProp.stageId.eq(stageId))
                .orderBy(qStageProp.displayOrder.asc())
                .fetch()
    }
}
