package io.sparkled.model.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.Table

@Entity
@Table(name = "song_audio")
class SongAudio {

    @Id
    @Column(name = "song_id")
    private var songId: Int? = null

    @Lob
    @Column(name = "audio_data")
    private var audioData: ByteArray? = null

    fun getSongId(): Int? {
        return songId
    }

    fun setSongId(songId: Int?): SongAudio {
        this.songId = songId
        return this
    }

    fun getAudioData(): ByteArray? {
        return audioData
    }

    fun setAudioData(audioData: ByteArray): SongAudio {
        this.audioData = audioData
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SongAudio

        if (songId != other.songId) return false

        return true
    }

    override fun hashCode(): Int {
        return songId ?: 0
    }

    override fun toString(): String {
        return "SongAudio(songId=$songId)"
    }
}
