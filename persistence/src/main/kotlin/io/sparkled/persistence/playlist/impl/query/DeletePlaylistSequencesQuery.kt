package io.sparkled.persistence.playlist.impl.query

import io.sparkled.model.entity.QPlaylistSequence.playlistSequence
import io.sparkled.model.util.IdUtils.NO_UUIDS
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import org.slf4j.LoggerFactory
import java.util.UUID

class DeletePlaylistSequencesQuery(playlistSequenceUuids: Collection<UUID>) : PersistenceQuery<Unit> {

    private val playlistSequenceUuids: Collection<UUID> =
        if (playlistSequenceUuids.isEmpty()) NO_UUIDS else playlistSequenceUuids

    override fun perform(queryFactory: QueryFactory) {
        val deleted = queryFactory
            .delete(playlistSequence)
            .where(playlistSequence.uuid.`in`(playlistSequenceUuids))
            .execute()

        logger.info("Deleted {} playlist sequence(s).", deleted)
    }

    companion object {

        private val logger = LoggerFactory.getLogger(DeletePlaylistSequencesQuery::class.java)
    }
}
