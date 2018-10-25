package io.sparkled.persistence.stage.impl.query

import io.sparkled.model.entity.StageProp
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.PersistenceQuery.Companion.qStageProp
import io.sparkled.persistence.QueryFactory
import java.util.*

class GetStagePropByUuidQuery(private val stageId: Int, private val uuid: UUID) : PersistenceQuery<Optional<StageProp>> {

    override fun perform(queryFactory: QueryFactory): Optional<StageProp> {
        val sequenceChannel = queryFactory
                .selectFrom(qStageProp)
                .where(qStageProp.stageId.eq(stageId).and(qStageProp.uuid.eq(uuid)))
                .fetchFirst()

        return Optional.ofNullable(sequenceChannel)
    }
}
