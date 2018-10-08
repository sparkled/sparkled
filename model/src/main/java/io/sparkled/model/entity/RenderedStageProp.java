package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "rendered_stage_prop")
public class RenderedStageProp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "sequence_id")
    private Integer sequenceId;

    @Column(name = "stage_prop_code")
    private String stagePropCode;

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

    public String getStagePropCode() {
        return stagePropCode;
    }

    public RenderedStageProp setStagePropCode(String stagePropCode) {
        this.stagePropCode = stagePropCode;
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
                Objects.equals(stagePropCode, that.stagePropCode) &&
                Objects.equals(ledCount, that.ledCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sequenceId, stagePropCode, ledCount);
    }

    @Override
    public String toString() {
        return "RenderedStageProp{" +
                "id=" + id +
                ", sequenceId=" + sequenceId +
                ", stagePropCode='" + stagePropCode + '\'' +
                ", ledCount=" + ledCount +
                '}';
    }
}
