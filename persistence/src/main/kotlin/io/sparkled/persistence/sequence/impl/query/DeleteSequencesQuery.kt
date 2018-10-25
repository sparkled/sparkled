package io.sparkled.persistence.sequence.impl.query

import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.playlist.impl.query.DeletePlaylistSequencesQuery
import io.sparkled.persistence.stage.impl.query.DeleteRenderedStagePropsQuery
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID

class DeleteSequencesQuery(sequenceIds: Collection<Integer>) : PersistenceQuery<Void> {

    private val sequenceIds: Collection<Integer>

    init {
        this.sequenceIds = if (sequenceIds.isEmpty()) noIds else sequenceIds
    }

    @Override
    fun perform(queryFactory: QueryFactory): Void? {
        deletePlaylistSequences(queryFactory)
        deleteRenderedStageProps(queryFactory)
        deleteSequenceChannels(queryFactory)
        deleteSequences(queryFactory)
        return null
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
