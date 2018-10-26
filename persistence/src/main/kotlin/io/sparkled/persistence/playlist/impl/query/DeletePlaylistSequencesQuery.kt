package io.sparkled.persistence.playlist.impl.query

import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.PersistenceQuery.Companion.noUuids
import io.sparkled.persistence.PersistenceQuery.Companion.qPlaylistSequence
import io.sparkled.persistence.QueryFactory
import org.slf4j.LoggerFactory
import java.util.*

class DeletePlaylistSequencesQuery(playlistSequenceUuids: Collection<UUID>) : PersistenceQuery<Unit> {

    private val playlistSequenceUuids: Collection<UUID>

    init {
        this.playlistSequenceUuids = if (playlistSequenceUuids.isEmpty()) noUuids else playlistSequenceUuids
    }

    override fun perform(queryFactory: QueryFactory) {
        val deleted = queryFactory
                .delete(qPlaylistSequence)
                .where(qPlaylistSequence.uuid.`in`(playlistSequenceUuids))
                .execute()

        logger.info("Deleted {} playlist sequence(s).", deleted)
    }

    companion object {

        private val logger = LoggerFactory.getLogger(DeletePlaylistSequencesQuery::class.java)
    }
}
