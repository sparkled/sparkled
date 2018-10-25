package io.sparkled.persistence.sequence.impl.query

import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID

class DeleteSequenceChannelsQuery(sequenceChannelUuids: Collection<UUID>) : PersistenceQuery<Void> {

    private val sequenceChannelUuids: Collection<UUID>

    init {
        this.sequenceChannelUuids = if (sequenceChannelUuids.isEmpty()) noUuids else sequenceChannelUuids
    }

    @Override
    fun perform(queryFactory: QueryFactory): Void? {
        val deleted = queryFactory
                .delete(qSequenceChannel)
                .where(qSequenceChannel.uuid.`in`(sequenceChannelUuids))
                .execute()

        logger.info("Deleted {} sequence channel(s).", deleted)
        return null
    }

    companion object {

        private val logger = LoggerFactory.getLogger(DeleteSequenceChannelsQuery::class.java)
    }
}
