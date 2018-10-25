package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.SequenceChannel
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.PersistenceQuery.Companion.qSequenceChannel
import io.sparkled.persistence.QueryFactory
import java.util.*

class GetSequenceChannelByUuidQuery(private val sequenceId: Int, private val uuid: UUID) : PersistenceQuery<Optional<SequenceChannel>> {

    override fun perform(queryFactory: QueryFactory): Optional<SequenceChannel> {
        val sequenceChannel = queryFactory
                .selectFrom(qSequenceChannel)
                .where(qSequenceChannel.sequenceId.eq(sequenceId).and(qSequenceChannel.uuid.eq(uuid)))
                .fetchFirst()

        return Optional.ofNullable(sequenceChannel)
    }
}
