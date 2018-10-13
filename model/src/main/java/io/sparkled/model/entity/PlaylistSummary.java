package io.sparkled.model.entity;

import java.util.Objects;

public class PlaylistSummary {

    private Integer sequenceCount;
    private Integer durationSeconds;

    public Integer getSequenceCount() {
        return sequenceCount;
    }

    public PlaylistSummary setSequenceCount(Integer sequenceCount) {
        this.sequenceCount = sequenceCount;
        return this;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public PlaylistSummary setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistSummary that = (PlaylistSummary) o;
        return Objects.equals(sequenceCount, that.sequenceCount) &&
                Objects.equals(durationSeconds, that.durationSeconds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequenceCount, durationSeconds);
    }

    @Override
    public String toString() {
        return "PlaylistSummary{" +
                "sequenceCount=" + sequenceCount +
                ", durationSeconds=" + durationSeconds +
                '}';
    }
}
