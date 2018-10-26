package io.sparkled.persistence.playlist.impl.query

import io.sparkled.model.entity.Playlist
import io.sparkled.model.entity.PlaylistSequence
import io.sparkled.model.validator.PlaylistSequenceValidator
import io.sparkled.model.validator.exception.EntityValidationException
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.PersistenceQuery.Companion.noUuids
import io.sparkled.persistence.PersistenceQuery.Companion.qPlaylistSequence
import io.sparkled.persistence.QueryFactory
import org.slf4j.LoggerFactory
import java.util.UUID

class SavePlaylistSequencesQuery(private val playlist: Playlist, private val playlistSequences: List<PlaylistSequence>) : PersistenceQuery<Unit> {

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
        uuidsToCheck = if (uuidsToCheck.isEmpty()) noUuids else uuidsToCheck

        val uuidsInUse = queryFactory.select(qPlaylistSequence)
                .from(qPlaylistSequence)
                .where(qPlaylistSequence.playlistId.ne(playlist.getId()).and(qPlaylistSequence.uuid.`in`(uuidsToCheck)))
                .fetchCount()
        return uuidsInUse > 0
    }

    private fun deleteRemovedPlaylistSequences(queryFactory: QueryFactory) {
        val uuidsToDelete = getPlaylistSequenceUuidsToDelete(queryFactory)
        DeletePlaylistSequencesQuery(uuidsToDelete).perform(queryFactory)
    }

    private fun getPlaylistSequenceUuidsToDelete(queryFactory: QueryFactory): List<UUID> {
        var uuidsToKeep = playlistSequences.asSequence().map(PlaylistSequence::getUuid).toList()
        uuidsToKeep = if (uuidsToKeep.isEmpty()) noUuids else uuidsToKeep

        return queryFactory
                .select(qPlaylistSequence.uuid)
                .from(qPlaylistSequence)
                .where(qPlaylistSequence.playlistId.eq(playlist.getId()).and(qPlaylistSequence.uuid.notIn(uuidsToKeep)))
                .fetch()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SavePlaylistSequencesQuery::class.java)
    }
}
