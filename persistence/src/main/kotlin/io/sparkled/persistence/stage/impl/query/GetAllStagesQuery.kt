package io.sparkled.persistence.stage.impl.query

import io.sparkled.model.entity.QStage.Companion.stage
import io.sparkled.model.entity.Stage
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetAllStagesQuery : PersistenceQuery<List<Stage>> {

    override fun perform(queryFactory: QueryFactory): List<Stage> {
        return queryFactory
            .selectFrom(stage)
            .orderBy(stage.name.asc())
            .fetch()
    }
}
