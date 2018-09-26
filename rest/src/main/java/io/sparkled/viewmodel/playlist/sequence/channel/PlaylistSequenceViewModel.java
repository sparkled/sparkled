package io.sparkled.viewmodel.playlist.sequence.channel;

import io.sparkled.model.animation.effect.Effect;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PlaylistSequenceViewModel {

    private UUID uuid;
    private int sequenceId;
    private UUID stagePropUuid;
    private String name;
    private Integer displayOrder;
    private List<Effect> effects;

    public UUID getUuid() {
        return uuid;
    }

    public PlaylistSequenceViewModel setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public PlaylistSequenceViewModel setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
        return this;
    }

    public UUID getStagePropUuid() {
        return stagePropUuid;
    }

    public PlaylistSequenceViewModel setStagePropUuid(UUID stagePropUuid) {
        this.stagePropUuid = stagePropUuid;
        return this;
    }

    public String getName() {
        return name;
    }

    public PlaylistSequenceViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public PlaylistSequenceViewModel setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public PlaylistSequenceViewModel setEffects(List<Effect> effects) {
        this.effects = effects;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaylistSequenceViewModel)) return false;
        PlaylistSequenceViewModel that = (PlaylistSequenceViewModel) o;
        return sequenceId == that.sequenceId &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(stagePropUuid, that.stagePropUuid) &&
                Objects.equals(name, that.name) &&
                Objects.equals(displayOrder, that.displayOrder) &&
                Objects.equals(effects, that.effects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, sequenceId, stagePropUuid, name, displayOrder, effects);
    }
}
