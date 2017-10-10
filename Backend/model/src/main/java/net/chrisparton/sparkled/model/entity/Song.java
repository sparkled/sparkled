package net.chrisparton.sparkled.model.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "song")
public class Song {

    private Integer id;
    private String name;
    private String artist;
    private String album;
    private int durationFrames;
    private int framesPerSecond;
    private SongStatus status;

    public Song() {
        this.framesPerSecond = 60;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 64)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "artist", nullable = false, length = 64)
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Basic
    @Column(name = "album", nullable = false, length = 64)
    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    @Basic
    @Column(name = "duration_frames", nullable = false)
    public int getDurationFrames() {
        return durationFrames;
    }

    public void setDurationFrames(int durationFrames) {
        this.durationFrames = durationFrames;
    }

    @Basic
    @Column(name = "frames_per_second", nullable = false)
    public int getFramesPerSecond() {
        return framesPerSecond;
    }

    public void setFramesPerSecond(int framesPerSecond) {
        this.framesPerSecond = framesPerSecond;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    public SongStatus getStatus() {
        return status;
    }

    public void setStatus(SongStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(id, song.id) &&
                durationFrames == song.durationFrames &&
                framesPerSecond == song.framesPerSecond &&
                Objects.equals(name, song.name) &&
                Objects.equals(artist, song.artist) &&
                Objects.equals(status, song.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, artist, durationFrames, framesPerSecond, status);
    }
}
