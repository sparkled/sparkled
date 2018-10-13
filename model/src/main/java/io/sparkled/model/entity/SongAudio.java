package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "song_audio")
public class SongAudio {

    @Id
    @Column(name = "song_id")
    private Integer songId;

    @Lob
    @Column(name = "audio_data")
    private byte[] audioData;

    public Integer getSongId() {
        return songId;
    }

    public SongAudio setSongId(Integer songId) {
        this.songId = songId;
        return this;
    }

    public byte[] getAudioData() {
        return audioData;
    }

    public SongAudio setAudioData(byte[] audioData) {
        this.audioData = audioData;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongAudio audio = (SongAudio) o;
        return Objects.equals(songId, audio.songId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songId);
    }

    @Override
    public String toString() {
        return "SongAudio{" +
                "songId=" + songId +
                '}';
    }
}
