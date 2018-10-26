package io.sparkled.persistence.song.impl.query

import io.sparkled.model.entity.Song
import io.sparkled.model.validator.SongValidator
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import org.slf4j.LoggerFactory

class SaveSongQuery(private val song: Song) : PersistenceQuery<Song> {

    override fun perform(queryFactory: QueryFactory): Song {
        SongValidator().validate(song)

        val entityManager = queryFactory.entityManager
        val savedSong = entityManager.merge(song)

        logger.info("Saved song {} ({}).", savedSong.getId(), savedSong.getName())
        return savedSong
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SaveSongQuery::class.java)
    }
}
