package io.sparkled.model.entity;

import io.sparkled.model.animation.SequenceChannelEffects;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "sequence_channel")
public class SequenceChannel {

    @Id
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Basic
    @Column(name = "sequence_id", nullable = false)
    private int sequenceId;

    @Basic
    @Column(name = "stage_prop_uuid", nullable = false)
    private UUID stagePropUuid;

    @Basic
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Basic
    @Column(name = "channel_json", columnDefinition = "text")
    private String channelJson;

    public UUID getUuid() {
        return uuid;
    }

    public SequenceChannel setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public SequenceChannel setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
        return this;
    }

    public UUID getStagePropUuid() {
        return stagePropUuid;
    }

    public SequenceChannel setStagePropUuid(UUID stagePropUuid) {
        this.stagePropUuid = stagePropUuid;
        return this;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public SequenceChannel setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    /**
     * @return a {@link SequenceChannelEffects} instance in JSON form
     */
    public String getChannelJson() {
        return channelJson;
    }

    /**
     * @param channelJson a {@link SequenceChannelEffects} instance in JSON form
     */
    public SequenceChannel setChannelJson(String channelJson) {
        this.channelJson = channelJson;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SequenceChannel that = (SequenceChannel) o;
        return sequenceId == that.sequenceId &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(stagePropUuid, that.stagePropUuid) &&
                Objects.equals(displayOrder, that.displayOrder) &&
                Objects.equals(channelJson, that.channelJson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, sequenceId, stagePropUuid, displayOrder, channelJson);
    }
}
