package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "song_audio")
public class SongAudio {

    @Id
    @Column(name = "sequence_id")
    private Integer sequenceId;

    @Lob
    @Column(name = "audio_data")
    private byte[] audioData;


    public Integer getSequenceId() {
        return sequenceId;
    }

    public SongAudio setSequenceId(Integer sequenceId) {
        this.sequenceId = sequenceId;
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
        return Objects.equals(sequenceId, audio.sequenceId) &&
                Arrays.equals(audioData, audio.audioData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequenceId, audioData);
    }

    @Override
    public String toString() {
        return "SongAudio{" +
                "sequenceId=" + sequenceId +
                ", audioData=" + Arrays.toString(audioData) +
                '}';
    }
}
