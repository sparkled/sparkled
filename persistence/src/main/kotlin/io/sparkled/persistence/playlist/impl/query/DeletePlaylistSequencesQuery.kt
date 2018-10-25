package io.sparkled.persistence.playlist.impl.query

import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID

class DeletePlaylistSequencesQuery(playlistSequenceUuids: Collection<UUID>) : PersistenceQuery<Void> {

    private val playlistSequenceUuids: Collection<UUID>

    init {
        this.playlistSequenceUuids = if (playlistSequenceUuids.isEmpty()) noUuids else playlistSequenceUuids
    }

    @Override
    fun perform(queryFactory: QueryFactory): Void? {
        val deleted = queryFactory
                .delete(qPlaylistSequence)
                .where(qPlaylistSequence.uuid.`in`(playlistSequenceUuids))
                .execute()

        logger.info("Deleted {} playlist sequence(s).", deleted)
        return null
    }

    companion object {

        private val logger = LoggerFactory.getLogger(DeletePlaylistSequencesQuery::class.java)
    }
}
