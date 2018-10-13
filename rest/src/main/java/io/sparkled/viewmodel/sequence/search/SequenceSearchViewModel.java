package io.sparkled.viewmodel.sequence.search;

import io.sparkled.model.entity.SequenceStatus;
import io.sparkled.viewmodel.ViewModel;

import java.util.Objects;

public class SequenceSearchViewModel implements ViewModel {
    private Integer id;
    private String name;
    private String songName;
    private String stageName;
    private Integer framesPerSecond;
    private Integer durationSeconds;
    private SequenceStatus status;

    public Integer getId() {
        return id;
    }

    public SequenceSearchViewModel setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public SequenceSearchViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getSongName() {
        return songName;
    }

    public SequenceSearchViewModel setSongName(String songName) {
        this.songName = songName;
        return this;
    }

    public String getStageName() {
        return stageName;
    }

    public SequenceSearchViewModel setStageName(String stageName) {
        this.stageName = stageName;
        return this;
    }

    public Integer getFramesPerSecond() {
        return framesPerSecond;
    }

    public SequenceSearchViewModel setFramesPerSecond(Integer framesPerSecond) {
        this.framesPerSecond = framesPerSecond;
        return this;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public SequenceSearchViewModel setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
        return this;
    }

    public SequenceStatus getStatus() {
        return status;
    }

    public SequenceSearchViewModel setStatus(SequenceStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SequenceSearchViewModel that = (SequenceSearchViewModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(songName, that.songName) &&
                Objects.equals(stageName, that.stageName) &&
                Objects.equals(framesPerSecond, that.framesPerSecond) &&
                Objects.equals(durationSeconds, that.durationSeconds) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, songName, stageName, framesPerSecond, durationSeconds, status);
    }

    @Override
    public String toString() {
        return "SequenceSearchViewModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", songName='" + songName + '\'' +
                ", stageName='" + stageName + '\'' +
                ", framesPerSecond=" + framesPerSecond +
                ", durationSeconds=" + durationSeconds +
                ", status=" + status +
                '}';
    }
}
