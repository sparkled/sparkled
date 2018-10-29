package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.QSequenceChannel.sequenceChannel
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import java.util.Optional
import java.util.UUID

class GetSequenceChannelByUuidQuery(private val sequenceId: Int, private val uuid: UUID) :
    PersistenceQuery<Optional<SequenceChannel>> {

    override fun perform(queryFactory: QueryFactory): Optional<SequenceChannel> {
        val sequenceChannel = queryFactory
            .selectFrom(sequenceChannel)
            .where(sequenceChannel.sequenceId.eq(sequenceId).and(sequenceChannel.uuid.eq(uuid)))
            .fetchFirst()

        return Optional.ofNullable(sequenceChannel)
    }
}
