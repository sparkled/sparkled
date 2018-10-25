package io.sparkled.persistence.song.impl.query

import io.sparkled.model.entity.Song
import io.sparkled.model.entity.SongAudio
import io.sparkled.model.validator.SongAudioValidator
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.persistence.EntityManager

class SaveSongAudioQuery(private val song: Song, private val audioData: ByteArray) : PersistenceQuery<Void> {

    @Override
    fun perform(queryFactory: QueryFactory): Void? {
        val songAudio = SongAudio().setSongId(song.getId()).setAudioData(audioData)
        SongAudioValidator().validate(songAudio)

        val entityManager = queryFactory.getEntityManager()
        val savedSongAudio = entityManager.merge(songAudio)

        logger.info("Saved song audio {} for song {}.", savedSongAudio.getSongId(), song.getName())
        return null
    }

    companion object {

        private val logger = LoggerFactory.getLogger(SaveSongQuery::class.java)
    }
}
