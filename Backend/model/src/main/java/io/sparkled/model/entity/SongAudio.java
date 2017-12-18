package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "song_audio")
public class SongAudio {

    private int songId;
    private byte[] audioData;

    @Id
    @Column(name = "song_id", nullable = false)
    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    @Lob
    @Column(name = "audio_data", nullable = false)
    public byte[] getAudioData() {
        return audioData;
    }

    public void setAudioData(byte[] audioData) {
        this.audioData = audioData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongAudio songAudio = (SongAudio) o;
        return songId == songAudio.songId &&
                Arrays.equals(audioData, songAudio.audioData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songId, audioData);
    }
}
