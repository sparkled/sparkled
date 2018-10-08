package io.sparkled.model.entity;

import io.sparkled.model.animation.SequenceChannelEffects;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "sequence_channel")
public class SequenceChannel {

    @Id
    @Column(name = "uuid")
    private UUID uuid;

    @Basic
    @Column(name = "sequence_id")
    private Integer sequenceId;

    @Basic
    @Column(name = "stage_prop_uuid")
    private UUID stagePropUuid;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "display_order")
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

    public Integer getSequenceId() {
        return sequenceId;
    }

    public SequenceChannel setSequenceId(Integer sequenceId) {
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

    public String getName() {
        return name;
    }

    public SequenceChannel setName(String name) {
        this.name = name;
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
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(sequenceId, that.sequenceId) &&
                Objects.equals(stagePropUuid, that.stagePropUuid) &&
                Objects.equals(name, that.name) &&
                Objects.equals(displayOrder, that.displayOrder) &&
                Objects.equals(channelJson, that.channelJson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, sequenceId, stagePropUuid, name, displayOrder, channelJson);
    }

    @Override
    public String toString() {
        return "SequenceChannel{" +
                "uuid=" + uuid +
                ", sequenceId=" + sequenceId +
                ", stagePropUuid=" + stagePropUuid +
                ", name='" + name + '\'' +
                ", displayOrder=" + displayOrder +
                ", channelJson='" + channelJson + '\'' +
                '}';
    }
}
