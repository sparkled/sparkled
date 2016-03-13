package net.chrisparton.xmas.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "song_data", schema = "xmas", catalog = "postgres")
public class SongData {

    private int songId;
    private byte[] mp3Data;

    @Id
    @Column(name = "song_id", nullable = false)
    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    @Basic
    @Column(name = "mp3_data", nullable = false)
    public byte[] getMp3Data() {
        return mp3Data;
    }

    public void setMp3Data(byte[] mp3Data) {
        this.mp3Data = mp3Data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongData songData = (SongData) o;
        return songId == songData.songId &&
                Arrays.equals(mp3Data, songData.mp3Data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songId, mp3Data);
    }
}
