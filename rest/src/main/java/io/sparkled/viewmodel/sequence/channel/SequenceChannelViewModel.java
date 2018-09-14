package io.sparkled.viewmodel.sequence.channel;

import io.sparkled.model.animation.SequenceChannelEffects;
import io.sparkled.model.animation.effect.Effect;

import java.util.List;
import java.util.UUID;

public class SequenceChannelViewModel {
    private UUID uuid;
    private String name;
    private int sequenceId;
    private UUID stagePropUuid;
    private Integer displayOrder;
    private List<Effect> effects;

    public UUID getUuid() {
        return uuid;
    }

    public SequenceChannelViewModel setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getName() {
        return name;
    }

    public SequenceChannelViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public SequenceChannelViewModel setSequenceId(int sequenceId) {
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
}
