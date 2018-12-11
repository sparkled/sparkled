package io.sparkled.persistence.song.impl

import io.sparkled.model.entity.Song
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.song.SongPersistenceService
import io.sparkled.persistence.song.impl.query.DeleteSongsQuery
import io.sparkled.persistence.song.impl.query.GetAllSongsQuery
import io.sparkled.persistence.song.impl.query.GetSongByIdQuery
import io.sparkled.persistence.song.impl.query.GetSongBySequenceIdQuery
import io.sparkled.persistence.song.impl.query.SaveSongAudioQuery
import io.sparkled.persistence.song.impl.query.SaveSongQuery
import javax.inject.Inject

class SongPersistenceServiceImpl
@Inject constructor(private val queryFactory: QueryFactory) : SongPersistenceService {

    override fun createSong(song: Song, audioData: ByteArray): Song {
        val savedSong = SaveSongQuery(song).perform(queryFactory)
        SaveSongAudioQuery(savedSong, audioData).perform(queryFactory)
        return savedSong
    }

    override fun getAllSongs(): List<Song> {
        return GetAllSongsQuery().perform(queryFactory)
    }

    override fun getSongById(songId: Int): Song? {
        return GetSongByIdQuery(songId).perform(queryFactory)
    }

    override fun getSongBySequenceId(sequenceId: Int): Song? {
        return GetSongBySequenceIdQuery(sequenceId).perform(queryFactory)
    }

    override fun deleteSong(songId: Int) {
        DeleteSongsQuery(listOf(songId)).perform(queryFactory)
    }
}
