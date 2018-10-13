package io.sparkled.viewmodel.playlist.search;

import io.sparkled.viewmodel.ViewModel;

import java.util.Objects;

public class PlaylistSearchViewModel implements ViewModel {

    private Integer id;
    private String name;
    private Integer sequenceCount;
    private Integer durationSeconds;

    public Integer getId() {
        return id;
    }

    public PlaylistSearchViewModel setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PlaylistSearchViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getSequenceCount() {
        return sequenceCount;
    }

    public PlaylistSearchViewModel setSequenceCount(Integer sequenceCount) {
        this.sequenceCount = sequenceCount;
        return this;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public PlaylistSearchViewModel setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistSearchViewModel that = (PlaylistSearchViewModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(sequenceCount, that.sequenceCount) &&
                Objects.equals(durationSeconds, that.durationSeconds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sequenceCount, durationSeconds);
    }

    @Override
    public String toString() {
        return "PlaylistSearchViewModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sequenceCount=" + sequenceCount +
                ", durationSeconds=" + durationSeconds +
                '}';
    }
}
