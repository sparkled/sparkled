package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.QPlaylistSequence.playlistSequence
import io.sparkled.model.entity.QRenderedStageProp.renderedStageProp
import io.sparkled.model.entity.QSequence.sequence
import io.sparkled.model.entity.QSequenceChannel.sequenceChannel
import io.sparkled.model.util.IdUtils.NO_IDS
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.playlist.impl.query.DeletePlaylistSequencesQuery
import io.sparkled.persistence.stage.impl.query.DeleteRenderedStagePropsQuery
import org.slf4j.LoggerFactory

class DeleteSequencesQuery(sequenceIds: Collection<Int>) : PersistenceQuery<Unit> {

    private val sequenceIds: Collection<Int> = if (sequenceIds.isEmpty()) NO_IDS else sequenceIds

    override fun perform(queryFactory: QueryFactory) {
        deletePlaylistSequences(queryFactory)
        deleteRenderedStageProps(queryFactory)
        deleteSequenceChannels(queryFactory)
        deleteSequences(queryFactory)
    }

    private fun deletePlaylistSequences(queryFactory: QueryFactory) {
        val playlistSequenceUuids = queryFactory
            .select(playlistSequence.uuid)
            .from(playlistSequence)
            .where(playlistSequence.sequenceId.`in`(sequenceIds))
            .fetch()
        DeletePlaylistSequencesQuery(playlistSequenceUuids).perform(queryFactory)
    }

    private fun deleteRenderedStageProps(queryFactory: QueryFactory) {
        val renderedStagePropIds = queryFactory
            .select(renderedStageProp.id)
            .from(renderedStageProp)
            .where(renderedStageProp.sequenceId.`in`(sequenceIds))
            .fetch()
        DeleteRenderedStagePropsQuery(renderedStagePropIds).perform(queryFactory)
    }

    private fun deleteSequenceChannels(queryFactory: QueryFactory) {
        val sequenceChannelUuids = queryFactory
            .select(sequenceChannel.uuid)
            .from(sequenceChannel)
            .where(sequenceChannel.sequenceId.`in`(sequenceIds))
            .fetch()
        DeleteSequenceChannelsQuery(sequenceChannelUuids).perform(queryFactory)
    }

    private fun deleteSequences(queryFactory: QueryFactory) {
        val deleted = queryFactory
            .delete(sequence)
            .where(sequence.id.`in`(sequenceIds))
            .execute()

        logger.info("Deleted {} sequence(s).", deleted)
    }

    companion object {

        private val logger = LoggerFactory.getLogger(DeleteSequencesQuery::class.java)
    }
}
