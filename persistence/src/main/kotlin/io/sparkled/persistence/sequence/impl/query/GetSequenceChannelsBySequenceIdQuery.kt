package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.QSequenceChannel.sequenceChannel
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetSequenceChannelsBySequenceIdQuery(private val sequenceId: Int) : PersistenceQuery<List<SequenceChannel>> {

    override fun perform(queryFactory: QueryFactory): List<SequenceChannel> {
        return queryFactory
            .selectFrom(sequenceChannel)
            .where(sequenceChannel.sequenceId.eq(sequenceId))
            .orderBy(sequenceChannel.displayOrder.asc())
            .fetch()
    }
}
