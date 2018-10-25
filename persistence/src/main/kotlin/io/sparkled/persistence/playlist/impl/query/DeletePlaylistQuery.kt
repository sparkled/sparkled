package io.sparkled.persistence.playlist.impl.query

import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID

class DeletePlaylistQuery(private val playlistId: Int) : PersistenceQuery<Void> {

    @Override
    fun perform(queryFactory: QueryFactory): Void? {
        deletePlaylistSequences(queryFactory)
        deletePlaylist(queryFactory)
        return null
    }

    private fun deletePlaylistSequences(queryFactory: QueryFactory) {
        val playlistSequenceUuids = queryFactory
                .select(qPlaylistSequence.uuid)
                .from(qPlaylistSequence)
                .where(qPlaylistSequence.playlistId.eq(playlistId))
                .fetch()
        DeletePlaylistSequencesQuery(playlistSequenceUuids).perform(queryFactory)
    }

    private fun deletePlaylist(queryFactory: QueryFactory) {
        val deleted = queryFactory
                .delete(qPlaylist)
                .where(qPlaylist.id.eq(playlistId))
                .execute()

        logger.info("Deleted {} playlist(s).", deleted)
    }

    companion object {

        private val logger = LoggerFactory.getLogger(DeletePlaylistQuery::class.java)
    }
}
