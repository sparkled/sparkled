package io.sparkled.persistence.playlist.impl.query

import io.sparkled.model.entity.Playlist
import io.sparkled.model.entity.PlaylistSequence
import io.sparkled.model.entity.QPlaylistSequence.playlistSequence
import io.sparkled.model.util.IdUtils.NO_UUIDS
import io.sparkled.model.validator.PlaylistSequenceValidator
import io.sparkled.model.validator.exception.EntityValidationException
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import org.slf4j.LoggerFactory
import java.util.UUID

class SavePlaylistSequencesQuery(
    private val playlist: Playlist,
    private val playlistSequences: List<PlaylistSequence>
) : PersistenceQuery<Unit> {

    override fun perform(queryFactory: QueryFactory) {
        val playlistSequenceValidator = PlaylistSequenceValidator()
        playlistSequences.forEach { ps -> ps.setPlaylistId(playlist.getId()!!) }
        playlistSequences.forEach(playlistSequenceValidator::validate)

        if (uuidAlreadyInUse(queryFactory)) {
            throw EntityValidationException("Playlist sequence already exists on another playlist.")
        } else {
            val entityManager = queryFactory.entityManager
            playlistSequences.forEach { entityManager.merge(it) }
            logger.info("Saved {} playlist sequence(s) for playlist {}.", playlistSequences.size, playlist.getId())

            deleteRemovedPlaylistSequences(queryFactory)
        }
    }

    private fun uuidAlreadyInUse(queryFactory: QueryFactory): Boolean {
        var uuidsToCheck = playlistSequences.asSequence().map(PlaylistSequence::getUuid).toList()
        uuidsToCheck = if (uuidsToCheck.isEmpty()) NO_UUIDS else uuidsToCheck

        val uuidsInUse = queryFactory.select(playlistSequence)
            .from(playlistSequence)
            .where(playlistSequence.playlistId.ne(playlist.getId()).and(playlistSequence.uuid.`in`(uuidsToCheck)))
            .fetchCount()
        return uuidsInUse > 0
    }

    private fun deleteRemovedPlaylistSequences(queryFactory: QueryFactory) {
        val uuidsToDelete = getPlaylistSequenceUuidsToDelete(queryFactory)
        DeletePlaylistSequencesQuery(uuidsToDelete).perform(queryFactory)
    }

    private fun getPlaylistSequenceUuidsToDelete(queryFactory: QueryFactory): List<UUID> {
        var uuidsToKeep = playlistSequences.asSequence().map(PlaylistSequence::getUuid).toList()
        uuidsToKeep = if (uuidsToKeep.isEmpty()) NO_UUIDS else uuidsToKeep

        return queryFactory
            .select(playlistSequence.uuid)
            .from(playlistSequence)
            .where(playlistSequence.playlistId.eq(playlist.getId()).and(playlistSequence.uuid.notIn(uuidsToKeep)))
            .fetch()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SavePlaylistSequencesQuery::class.java)
    }
}
