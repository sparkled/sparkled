package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sequence")
public class Sequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "stage_id")
    private Integer stageId;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "artist")
    private String artist;

    @Basic
    @Column(name = "album")
    private String album;

    @Basic
    @Column(name = "duration_frames")
    private Integer durationFrames;

    @Basic
    @Column(name = "frames_per_second")
    private Integer framesPerSecond = 60;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
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

    public Integer getDurationFrames() {
        return durationFrames;
    }

    public Sequence setDurationFrames(Integer durationFrames) {
        this.durationFrames = durationFrames;
        return this;
    }

    public Integer getFramesPerSecond() {
        return framesPerSecond;
    }

    public Sequence setFramesPerSecond(Integer framesPerSecond) {
        this.framesPerSecond = framesPerSecond;
        return this;
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
                Objects.equals(name, sequence.name) &&
                Objects.equals(artist, sequence.artist) &&
                Objects.equals(album, sequence.album) &&
                Objects.equals(durationFrames, sequence.durationFrames) &&
                Objects.equals(framesPerSecond, sequence.framesPerSecond) &&
                status == sequence.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stageId, name, artist, album, durationFrames, framesPerSecond, status);
    }

    @Override
    public String toString() {
        return "Sequence{" +
                "id=" + id +
                ", stageId=" + stageId +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", durationFrames=" + durationFrames +
                ", framesPerSecond=" + framesPerSecond +
                ", status=" + status +
                '}';
    }
}
