package io.sparkled.persistence.stage.impl.query

import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.PersistenceQuery.Companion.noUuids
import io.sparkled.persistence.PersistenceQuery.Companion.qRenderedStageProp
import io.sparkled.persistence.PersistenceQuery.Companion.qSequenceChannel
import io.sparkled.persistence.PersistenceQuery.Companion.qStageProp
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.sequence.impl.query.DeleteSequenceChannelsQuery
import org.slf4j.LoggerFactory
import java.util.*

class DeleteStagePropsQuery internal constructor(stagePropUuids: Collection<UUID>) : PersistenceQuery<Unit> {

    private val stagePropUuids: Collection<UUID>

    init {
        this.stagePropUuids = if (stagePropUuids.isEmpty()) noUuids else stagePropUuids
    }

    override fun perform(queryFactory: QueryFactory) {
        deleteRenderedStageProps(queryFactory)
        deleteSequenceChannels(queryFactory)
        deleteStageProps(queryFactory)
    }

    private fun deleteRenderedStageProps(queryFactory: QueryFactory) {
        val renderedStagePropIds = queryFactory
                .select(qRenderedStageProp.id)
                .from(qRenderedStageProp)
                .where(qRenderedStageProp.stagePropUuid.`in`(stagePropUuids))
                .fetch()
        DeleteRenderedStagePropsQuery(renderedStagePropIds).perform(queryFactory)
    }

    private fun deleteSequenceChannels(queryFactory: QueryFactory) {
        val sequenceChannelUuids = queryFactory
                .select(qSequenceChannel.uuid)
                .from(qSequenceChannel)
                .where(qSequenceChannel.stagePropUuid.`in`(stagePropUuids))
                .fetch()
        DeleteSequenceChannelsQuery(sequenceChannelUuids).perform(queryFactory)
    }

    private fun deleteStageProps(queryFactory: QueryFactory) {
        val deleted = queryFactory
                .delete(qStageProp)
                .where(qStageProp.uuid.`in`(stagePropUuids))
                .execute()

        logger.info("Deleted {} stage prop(s).", deleted)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DeleteStagePropsQuery::class.java)
    }
}
