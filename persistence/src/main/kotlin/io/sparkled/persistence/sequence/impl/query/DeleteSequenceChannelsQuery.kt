package io.sparkled.persistence.sequence.impl.query

import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.PersistenceQuery.Companion.noUuids
import io.sparkled.persistence.PersistenceQuery.Companion.qSequenceChannel
import io.sparkled.persistence.QueryFactory
import org.slf4j.LoggerFactory
import java.util.*

class DeleteSequenceChannelsQuery(sequenceChannelUuids: Collection<UUID>) : PersistenceQuery<Unit> {

    private val sequenceChannelUuids: Collection<UUID>

    init {
        this.sequenceChannelUuids = if (sequenceChannelUuids.isEmpty()) noUuids else sequenceChannelUuids
    }

    override fun perform(queryFactory: QueryFactory) {
        val deleted = queryFactory
                .delete(qSequenceChannel)
                .where(qSequenceChannel.uuid.`in`(sequenceChannelUuids))
                .execute()

        logger.info("Deleted {} sequence channel(s).", deleted)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DeleteSequenceChannelsQuery::class.java)
    }
}
