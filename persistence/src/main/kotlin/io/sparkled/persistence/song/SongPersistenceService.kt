package io.sparkled.persistence.song

import io.sparkled.model.entity.Song
import java.util.*

interface SongPersistenceService {

    fun createSong(song: Song, audioData: ByteArray): Song

    fun getAllSongs(): List<Song>

    fun getSongById(songId: Int): Optional<Song>

    fun getSongBySequenceId(sequenceId: Int): Optional<Song>

    fun deleteSong(songId: Int)
}
