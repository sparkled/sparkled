package io.sparkled.persistence.playlist.impl.query

import io.sparkled.model.entity.QPlaylist.Companion.playlist
import io.sparkled.model.entity.QPlaylistSequence.Companion.playlistSequence
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import org.slf4j.LoggerFactory

class DeletePlaylistQuery(private val playlistId: Int) : PersistenceQuery<Unit> {

    override fun perform(queryFactory: QueryFactory) {
        deletePlaylistSequences(queryFactory)
        deletePlaylist(queryFactory)
    }

    private fun deletePlaylistSequences(queryFactory: QueryFactory) {
        val playlistSequenceUuids = queryFactory
            .select(playlistSequence.uuid)
            .from(playlistSequence)
            .where(playlistSequence.playlistId.eq(playlistId))
            .fetch()
        DeletePlaylistSequencesQuery(playlistSequenceUuids).perform(queryFactory)
    }

    private fun deletePlaylist(queryFactory: QueryFactory) {
        val deleted = queryFactory
            .delete(playlist)
            .where(playlist.id.eq(playlistId))
            .execute()

        logger.info("Deleted {} playlist(s).", deleted)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DeletePlaylistQuery::class.java)
    }
}
