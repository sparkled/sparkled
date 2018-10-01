package io.sparkled.viewmodel.playlist.sequence;

import java.util.Objects;
import java.util.UUID;

public class PlaylistSequenceViewModel {

    private UUID uuid;
    private Integer sequenceId;
    private Integer displayOrder;

    public UUID getUuid() {
        return uuid;
    }

    public PlaylistSequenceViewModel setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public Integer getSequenceId() {
        return sequenceId;
    }

    public PlaylistSequenceViewModel setSequenceId(Integer sequenceId) {
        this.sequenceId = sequenceId;
        return this;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public PlaylistSequenceViewModel setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistSequenceViewModel that = (PlaylistSequenceViewModel) o;
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(sequenceId, that.sequenceId) &&
                Objects.equals(displayOrder, that.displayOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, sequenceId, displayOrder);
    }

    @Override
    public String toString() {
        return "PlaylistSequenceViewModel{" +
                "uuid=" + uuid +
                ", sequenceId=" + sequenceId +
                ", displayOrder=" + displayOrder +
                '}';
    }
}
