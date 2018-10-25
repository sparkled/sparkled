package io.sparkled.persistence.song.impl

import io.sparkled.model.entity.Song
import io.sparkled.model.entity.SongAudio
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.sequence.impl.query.GetSongAudioBySequenceIdQuery
import io.sparkled.persistence.song.SongPersistenceService
import io.sparkled.persistence.song.impl.query.*

import javax.inject.Inject
import java.util.Collections
import java.util.Optional

class SongPersistenceServiceImpl @Inject
constructor(private val queryFactory: QueryFactory) : SongPersistenceService {

    @Override
    fun createSong(song: Song, audioData: ByteArray): Song {
        val savedSong = SaveSongQuery(song).perform(queryFactory)
        SaveSongAudioQuery(savedSong, audioData).perform(queryFactory)
        return savedSong
    }

    val allSongs: List<Song>
        @Override
        get() = GetAllSongsQuery().perform(queryFactory)

    @Override
    fun getSongById(songId: Int): Optional<Song> {
        return GetSongByIdQuery(songId).perform(queryFactory)
    }

    @Override
    fun getSongBySequenceId(sequenceId: Int): Optional<Song> {
        return GetSongBySequenceIdQuery(sequenceId).perform(queryFactory)
    }

    @Override
    fun deleteSong(songId: Int) {
        DeleteSongsQuery(Collections.singletonList(songId)).perform(queryFactory)
    }
}
