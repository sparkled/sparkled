package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "song")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "stage_id", nullable = false)
    private Integer stageId;

    @Basic
    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Basic
    @Column(name = "artist", nullable = false, length = 64)
    private String artist;

    @Basic
    @Column(name = "album", nullable = false, length = 64)
    private String album;

    @Basic
    @Column(name = "duration_frames", nullable = false)
    private int durationFrames;

    @Basic
    @Column(name = "frames_per_second", nullable = false)
    private int framesPerSecond = 60;

    @Basic
    @Column(name = "auto_schedulable", nullable = false)
    private boolean autoSchedulable;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private SongStatus status;

    public Integer getId() {
        return id;
    }

    public Song setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getStageId() {
        return stageId;
    }

    public Song setStageId(Integer stageId) {
        this.stageId = stageId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Song setName(String name) {
        this.name = name;
        return this;
    }

    public String getArtist() {
        return artist;
    }

    public Song setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getAlbum() {
        return album;
    }

    public Song setAlbum(String album) {
        this.album = album;
        return this;
    }

    public int getDurationFrames() {
        return durationFrames;
    }

    public Song setDurationFrames(int durationFrames) {
        this.durationFrames = durationFrames;
        return this;
    }

    public int getFramesPerSecond() {
        return framesPerSecond;
    }

    public Song setFramesPerSecond(int framesPerSecond) {
        this.framesPerSecond = framesPerSecond;
        return this;
    }

    public boolean isAutoSchedulable() {
        return autoSchedulable;
    }

    public void setAutoSchedulable(boolean autoSchedulable) {
        this.autoSchedulable = autoSchedulable;
    }

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
                Objects.equals(stageId, song.stageId) &&
                durationFrames == song.durationFrames &&
                framesPerSecond == song.framesPerSecond &&
                autoSchedulable == song.autoSchedulable &&
                Objects.equals(name, song.name) &&
                Objects.equals(artist, song.artist) &&
                Objects.equals(status, song.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stageId, name, artist, durationFrames, framesPerSecond, autoSchedulable, status);
    }
}
