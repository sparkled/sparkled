package io.sparkled.persistence.stage.impl.query

import io.sparkled.model.entity.QStageProp.Companion.stageProp
import io.sparkled.model.entity.StageProp
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import java.util.UUID

class GetStagePropByUuidQuery(private val stageId: Int, private val uuid: UUID) :
    PersistenceQuery<StageProp?> {

    override fun perform(queryFactory: QueryFactory): StageProp? {
        return queryFactory
            .selectFrom(stageProp)
            .where(stageProp.stageId.eq(stageId).and(stageProp.uuid.eq(uuid)))
            .fetchFirst()
    }
}
