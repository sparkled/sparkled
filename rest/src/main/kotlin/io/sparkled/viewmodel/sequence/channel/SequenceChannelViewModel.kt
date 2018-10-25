package io.sparkled.viewmodel.sequence.channel;

import io.sparkled.model.animation.effect.Effect;
import io.sparkled.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SequenceChannelViewModel implements ViewModel {

    private UUID uuid;
    private Integer sequenceId;
    private UUID stagePropUuid;
    private String name;
    private Integer displayOrder;
    private List<Effect> effects = new ArrayList<>();

    public UUID getUuid() {
        return uuid;
    }

    public SequenceChannelViewModel setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public Integer getSequenceId() {
        return sequenceId;
    }

    public SequenceChannelViewModel setSequenceId(Integer sequenceId) {
        this.sequenceId = sequenceId;
        return this;
    }

    public UUID getStagePropUuid() {
        return stagePropUuid;
    }

    public SequenceChannelViewModel setStagePropUuid(UUID stagePropUuid) {
        this.stagePropUuid = stagePropUuid;
        return this;
    }

    public String getName() {
        return name;
    }

    public SequenceChannelViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public SequenceChannelViewModel setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public SequenceChannelViewModel setEffects(List<Effect> effects) {
        this.effects = effects;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SequenceChannelViewModel)) return false;
        SequenceChannelViewModel that = (SequenceChannelViewModel) o;
        return Objects.equals(sequenceId, that.sequenceId) &&
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
