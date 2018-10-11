package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "rendered_stage_prop")
public class RenderedStageProp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "sequence_id")
    private Integer sequenceId;

    @Column(name = "stage_prop_uuid")
    private UUID stagePropUuid;

    @Column(name = "led_count")
    private Integer ledCount;

    @Lob
    @Column(name = "data")
    private byte[] data;

    public Integer getId() {
        return id;
    }

    public RenderedStageProp setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getSequenceId() {
        return sequenceId;
    }

    public RenderedStageProp setSequenceId(Integer sequenceId) {
        this.sequenceId = sequenceId;
        return this;
    }

    public UUID getStagePropUuid() {
        return stagePropUuid;
    }

    public RenderedStageProp setStagePropUuid(UUID stagePropUuid) {
        this.stagePropUuid = stagePropUuid;
        return this;
    }

    public Integer getLedCount() {
        return ledCount;
    }

    public RenderedStageProp setLedCount(Integer ledCount) {
        this.ledCount = ledCount;
        return this;
    }

    public byte[] getData() {
        return data;
    }

    public RenderedStageProp setData(byte[] data) {
        this.data = data;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenderedStageProp that = (RenderedStageProp) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(sequenceId, that.sequenceId) &&
                Objects.equals(stagePropUuid, that.stagePropUuid) &&
                Objects.equals(ledCount, that.ledCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sequenceId, stagePropUuid, ledCount);
    }

    @Override
    public String toString() {
        return "RenderedStageProp{" +
                "id=" + id +
                ", sequenceId=" + sequenceId +
                ", stagePropUuid='" + stagePropUuid + '\'' +
                ", ledCount=" + ledCount +
                '}';
    }
}
