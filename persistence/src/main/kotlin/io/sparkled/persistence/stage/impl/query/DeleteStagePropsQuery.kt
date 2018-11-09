package io.sparkled.persistence.stage.impl.query

import io.sparkled.model.entity.QRenderedStageProp.renderedStageProp
import io.sparkled.model.entity.QSequenceChannel.sequenceChannel
import io.sparkled.model.entity.QStageProp.stageProp
import io.sparkled.model.util.IdUtils.NO_UUIDS
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.sequence.impl.query.DeleteSequenceChannelsQuery
import org.slf4j.LoggerFactory
import java.util.UUID

class DeleteStagePropsQuery internal constructor(stagePropUuids: Collection<UUID>) : PersistenceQuery<Unit> {

    private val stagePropUuids: Collection<UUID> = if (stagePropUuids.isEmpty()) NO_UUIDS else stagePropUuids

    override fun perform(queryFactory: QueryFactory) {
        deleteRenderedStageProps(queryFactory)
        deleteSequenceChannels(queryFactory)
        deleteStageProps(queryFactory)
    }

    private fun deleteRenderedStageProps(queryFactory: QueryFactory) {
        val renderedStagePropIds = queryFactory
            .select(renderedStageProp.id)
            .from(renderedStageProp)
            .where(renderedStageProp.stagePropUuid.`in`(stagePropUuids))
            .fetch()
        DeleteRenderedStagePropsQuery(renderedStagePropIds).perform(queryFactory)
    }

    private fun deleteSequenceChannels(queryFactory: QueryFactory) {
        val sequenceChannelUuids = queryFactory
            .select(sequenceChannel.uuid)
            .from(sequenceChannel)
            .where(sequenceChannel.stagePropUuid.`in`(stagePropUuids))
            .fetch()
        DeleteSequenceChannelsQuery(sequenceChannelUuids).perform(queryFactory)
    }

    private fun deleteStageProps(queryFactory: QueryFactory) {
        val deleted = queryFactory
            .delete(stageProp)
            .where(stageProp.uuid.`in`(stagePropUuids))
            .execute()

        logger.info("Deleted {} stage prop(s).", deleted)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DeleteStagePropsQuery::class.java)
    }
}
