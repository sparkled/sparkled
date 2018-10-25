package io.sparkled.persistence.song

import io.sparkled.model.entity.Song
import io.sparkled.model.entity.SongAudio
import java.util.Optional

interface SongPersistenceService {

    fun createSong(song: Song, audioData: ByteArray): Song

    val allSongs: List<Song>

    fun getSongById(songId: Int): Optional<Song>

    fun getSongBySequenceId(sequenceId: Int): Optional<Song>

    fun deleteSong(songId: Int)
}
