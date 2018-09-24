package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "song_audio")
public class SongAudio {

    @Id
    @Column(name = "sequence_id", nullable = false)
    private int sequenceId;

    @Lob
    @Column(name = "audio_data", nullable = false)
    private byte[] audioData;

    public int getSequenceId() {
        return sequenceId;
    }

    public SongAudio setSequenceId(int sequenceId) {
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
        SongAudio songAudio = (SongAudio) o;
        return sequenceId == songAudio.sequenceId &&
                Arrays.equals(audioData, songAudio.audioData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequenceId, audioData);
    }
}
