package io.sparkled.persistence.song

import io.sparkled.model.entity.Song

interface SongPersistenceService {

    fun createSong(song: Song, audioData: ByteArray): Song

    fun getAllSongs(): List<Song>

    fun getSongById(songId: Int): Song?

    fun getSongBySequenceId(sequenceId: Int): Song?

    fun deleteSong(songId: Int)
}
