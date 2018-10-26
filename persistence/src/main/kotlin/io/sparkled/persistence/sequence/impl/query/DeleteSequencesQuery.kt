package io.sparkled.persistence.sequence.impl.query

import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.PersistenceQuery.Companion.noIds
import io.sparkled.persistence.PersistenceQuery.Companion.qPlaylistSequence
import io.sparkled.persistence.PersistenceQuery.Companion.qRenderedStageProp
import io.sparkled.persistence.PersistenceQuery.Companion.qSequence
import io.sparkled.persistence.PersistenceQuery.Companion.qSequenceChannel
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.playlist.impl.query.DeletePlaylistSequencesQuery
import io.sparkled.persistence.stage.impl.query.DeleteRenderedStagePropsQuery
import org.slf4j.LoggerFactory

class DeleteSequencesQuery(sequenceIds: Collection<Int>) : PersistenceQuery<Unit> {

    private val sequenceIds: Collection<Int>

    init {
        this.sequenceIds = if (sequenceIds.isEmpty()) noIds else sequenceIds
    }

    override fun perform(queryFactory: QueryFactory) {
        deletePlaylistSequences(queryFactory)
        deleteRenderedStageProps(queryFactory)
        deleteSequenceChannels(queryFactory)
        deleteSequences(queryFactory)
    }

    private fun deletePlaylistSequences(queryFactory: QueryFactory) {
        val playlistSequenceUuids = queryFactory
                .select(qPlaylistSequence.uuid)
                .from(qPlaylistSequence)
                .where(qPlaylistSequence.sequenceId.`in`(sequenceIds))
                .fetch()
        DeletePlaylistSequencesQuery(playlistSequenceUuids).perform(queryFactory)
    }

    private fun deleteRenderedStageProps(queryFactory: QueryFactory) {
        val renderedStagePropIds = queryFactory
                .select(qRenderedStageProp.id)
                .from(qRenderedStageProp)
                .where(qRenderedStageProp.sequenceId.`in`(sequenceIds))
                .fetch()
        DeleteRenderedStagePropsQuery(renderedStagePropIds).perform(queryFactory)
    }

    private fun deleteSequenceChannels(queryFactory: QueryFactory) {
        val sequenceChannelUuids = queryFactory
                .select(qSequenceChannel.uuid)
                .from(qSequenceChannel)
                .where(qSequenceChannel.sequenceId.`in`(sequenceIds))
                .fetch()
        DeleteSequenceChannelsQuery(sequenceChannelUuids).perform(queryFactory)
    }

    private fun deleteSequences(queryFactory: QueryFactory) {
        val deleted = queryFactory
                .delete(qSequence)
                .where(qSequence.id.`in`(sequenceIds))
                .execute()

        logger.info("Deleted {} sequence(s).", deleted)
    }

    companion object {

        private val logger = LoggerFactory.getLogger(DeleteSequencesQuery::class.java)
    }
}
