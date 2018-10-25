package io.sparkled.model.entity

import javax.persistence.*
import java.util.Objects

@Entity
@Table(name = "song_audio")
class SongAudio {

    @Id
    @Column(name = "song_id")
    private var songId: Integer? = null

    @Lob
    @Column(name = "audio_data")
    private var audioData: ByteArray? = null

    fun getSongId(): Integer {
        return songId
    }

    fun setSongId(songId: Integer): SongAudio {
        this.songId = songId
        return this
    }

    fun getAudioData(): ByteArray {
        return audioData
    }

    fun setAudioData(audioData: ByteArray): SongAudio {
        this.audioData = audioData
        return this
    }

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        return Objects.equals(songId, o!!.songId)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(songId)
    }

    @Override
    fun toString(): String {
        return "SongAudio{" +
                "songId=" + songId +
                '}'
    }
}
