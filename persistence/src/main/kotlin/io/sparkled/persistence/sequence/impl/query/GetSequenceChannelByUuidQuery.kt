package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.QSequenceChannel.Companion.sequenceChannel
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import java.util.UUID

class GetSequenceChannelByUuidQuery(private val sequenceId: Int, private val uuid: UUID) :
    PersistenceQuery<SequenceChannel?> {

    override fun perform(queryFactory: QueryFactory): SequenceChannel? {
        return queryFactory
            .selectFrom(sequenceChannel)
            .where(sequenceChannel.sequenceId.eq(sequenceId).and(sequenceChannel.uuid.eq(uuid)))
            .fetchFirst()
    }
}
