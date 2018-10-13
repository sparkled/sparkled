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
    @Column(name = "song_id")
    private Integer songId;

    @Basic
    @Column(name = "stage_id")
    private Integer stageId;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "frames_per_second")
    private Integer framesPerSecond;

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

    public Integer getSongId() {
        return songId;
    }

    public Sequence setSongId(Integer songId) {
        this.songId = songId;
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
                Objects.equals(songId, sequence.songId) &&
                Objects.equals(stageId, sequence.stageId) &&
                Objects.equals(name, sequence.name) &&
                Objects.equals(framesPerSecond, sequence.framesPerSecond) &&
                status == sequence.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, songId, stageId, name, framesPerSecond, status);
    }

    @Override
    public String toString() {
        return "Sequence{" +
                "id=" + id +
                ", songId=" + songId +
                ", stageId=" + stageId +
                ", name='" + name + '\'' +
                ", framesPerSecond=" + framesPerSecond +
                ", status=" + status +
                '}';
    }
}
