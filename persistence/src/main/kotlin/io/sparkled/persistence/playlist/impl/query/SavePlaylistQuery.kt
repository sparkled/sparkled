package io.sparkled.persistence.playlist.impl.query

import io.sparkled.model.entity.Playlist
import io.sparkled.model.validator.PlaylistValidator
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import org.slf4j.LoggerFactory

class SavePlaylistQuery(private val playlist: Playlist) : PersistenceQuery<Playlist> {

    override fun perform(queryFactory: QueryFactory): Playlist {
        PlaylistValidator().validate(playlist)

        val entityManager = queryFactory.entityManager
        val savedPlaylist = entityManager.merge(playlist)
        logger.info("Saved playlist {} ({}).", savedPlaylist.getId(), savedPlaylist.getName())
        return savedPlaylist!!
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SavePlaylistQuery::class.java)
    }
}
