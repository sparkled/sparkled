package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sequence")
public class Sequence {

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
    private SequenceStatus status;

    public Integer getId() {
        return id;
    }

    public Sequence setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getStageId() {
        return stageId;
    }

    public Sequence setStageId(Integer stageId) {
        this.stageId = stageId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Sequence setName(String name) {
        this.name = name;
        return this;
    }

    public String getArtist() {
        return artist;
    }

    public Sequence setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getAlbum() {
        return album;
    }

    public Sequence setAlbum(String album) {
        this.album = album;
        return this;
    }

    public int getDurationFrames() {
        return durationFrames;
    }

    public Sequence setDurationFrames(int durationFrames) {
        this.durationFrames = durationFrames;
        return this;
    }

    public int getFramesPerSecond() {
        return framesPerSecond;
    }

    public Sequence setFramesPerSecond(int framesPerSecond) {
        this.framesPerSecond = framesPerSecond;
        return this;
    }

    public boolean isAutoSchedulable() {
        return autoSchedulable;
    }

    public void setAutoSchedulable(boolean autoSchedulable) {
        this.autoSchedulable = autoSchedulable;
    }

    public SequenceStatus getStatus() {
        return status;
    }

    public Sequence setStatus(SequenceStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sequence sequence = (Sequence) o;
        return Objects.equals(id, sequence.id) &&
                Objects.equals(stageId, sequence.stageId) &&
                durationFrames == sequence.durationFrames &&
                framesPerSecond == sequence.framesPerSecond &&
                autoSchedulable == sequence.autoSchedulable &&
                Objects.equals(name, sequence.name) &&
                Objects.equals(artist, sequence.artist) &&
                Objects.equals(status, sequence.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stageId, name, artist, durationFrames, framesPerSecond, autoSchedulable, status);
    }
}
