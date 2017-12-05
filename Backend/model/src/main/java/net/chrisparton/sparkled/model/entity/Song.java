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
    private boolean autoSchedulable;
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

    public Song setId(Integer id) {
        this.id = id;
        return this;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 64)
    public String getName() {
        return name;
    }

    public Song setName(String name) {
        this.name = name;
        return this;
    }

    @Basic
    @Column(name = "artist", nullable = false, length = 64)
    public String getArtist() {
        return artist;
    }

    public Song setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    @Basic
    @Column(name = "album", nullable = false, length = 64)
    public String getAlbum() {
        return album;
    }

    public Song setAlbum(String album) {
        this.album = album;
        return this;
    }

    @Basic
    @Column(name = "duration_frames", nullable = false)
    public int getDurationFrames() {
        return durationFrames;
    }

    public Song setDurationFrames(int durationFrames) {
        this.durationFrames = durationFrames;
        return this;
    }

    @Basic
    @Column(name = "frames_per_second", nullable = false)
    public int getFramesPerSecond() {
        return framesPerSecond;
    }

    public Song setFramesPerSecond(int framesPerSecond) {
        this.framesPerSecond = framesPerSecond;
        return this;
    }

    @Basic
    @Column(name = "auto_schedulable", nullable = false)
    public boolean isAutoSchedulable() {
        return autoSchedulable;
    }

    public void setAutoSchedulable(boolean autoSchedulable) {
        this.autoSchedulable = autoSchedulable;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    public SongStatus getStatus() {
        return status;
    }

    public Song setStatus(SongStatus status) {
        this.status = status;
        return this;
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
