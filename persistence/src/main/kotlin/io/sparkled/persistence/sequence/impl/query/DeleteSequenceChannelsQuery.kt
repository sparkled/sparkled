package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.QSequenceChannel.Companion.sequenceChannel
import io.sparkled.model.util.IdUtils.NO_UUIDS
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import java.util.UUID
import org.slf4j.LoggerFactory

class DeleteSequenceChannelsQuery(sequenceChannelUuids: Collection<UUID>) : PersistenceQuery<Unit> {

    private val sequenceChannelUuids: Collection<UUID> =
        if (sequenceChannelUuids.isEmpty()) NO_UUIDS else sequenceChannelUuids

    override fun perform(queryFactory: QueryFactory) {
        val deleted = queryFactory
            .delete(sequenceChannel)
            .where(sequenceChannel.uuid.`in`(sequenceChannelUuids))
            .execute()

        logger.info("Deleted {} sequence channel(s).", deleted)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DeleteSequenceChannelsQuery::class.java)
    }
}
