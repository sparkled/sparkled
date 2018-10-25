package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.SequenceChannel
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetSequenceChannelsBySequenceIdQuery(private val sequenceId: Int) : PersistenceQuery<List<SequenceChannel>> {

    @Override
    fun perform(queryFactory: QueryFactory): List<SequenceChannel> {
        return queryFactory
                .selectFrom(qSequenceChannel)
                .where(qSequenceChannel.sequenceId.eq(sequenceId))
                .orderBy(qSequenceChannel.displayOrder.asc())
                .fetch()
    }
}
