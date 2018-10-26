package io.sparkled.persistence.song.impl

import io.sparkled.model.entity.Song
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.song.SongPersistenceService
import io.sparkled.persistence.song.impl.query.*
import java.util.*
import javax.inject.Inject

class SongPersistenceServiceImpl @Inject
constructor(private val queryFactory: QueryFactory) : SongPersistenceService {

    override fun createSong(song: Song, audioData: ByteArray): Song {
        val savedSong = SaveSongQuery(song).perform(queryFactory)
        SaveSongAudioQuery(savedSong, audioData).perform(queryFactory)
        return savedSong
    }

    override fun getAllSongs(): List<Song> {
        return GetAllSongsQuery().perform(queryFactory)
    }

    override fun getSongById(songId: Int): Optional<Song> {
        return GetSongByIdQuery(songId).perform(queryFactory)
    }

    override fun getSongBySequenceId(sequenceId: Int): Optional<Song> {
        return GetSongBySequenceIdQuery(sequenceId).perform(queryFactory)
    }

    override fun deleteSong(songId: Int) {
        DeleteSongsQuery(Collections.singletonList(songId)).perform(queryFactory)
    }
}
