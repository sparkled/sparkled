package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "rendered_stage_prop")
public class RenderedStageProp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "sequence_id", nullable = false)
    private int sequenceId;

    @Column(name = "stage_prop_code", nullable = false)
    private String stagePropCode;

    @Column(name = "led_count", nullable = false)
    private int ledCount;

    @Lob
    @Column(name = "data")
    private byte[] data;


    public int getId() {
        return id;
    }

    public RenderedStageProp setId(int id) {
        this.id = id;
        return this;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public RenderedStageProp setSequenceId(int sequenceId) {
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

    public int getLedCount() {
        return ledCount;
    }

    public RenderedStageProp setLedCount(int ledCount) {
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
        return sequenceId == that.sequenceId &&
                Objects.equals(stagePropCode, that.stagePropCode) &&
                Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequenceId, stagePropCode, data);
    }
}
