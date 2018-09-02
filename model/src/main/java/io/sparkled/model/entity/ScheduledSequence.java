package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "scheduled_sequence")
public class ScheduledSequence {

    public static final int MIN_SECONDS_BETWEEN_SEQUENCES = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    private Sequence sequence;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time", nullable = false)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time", nullable = false)
    private Date endTime;

    public ScheduledSequence() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ScheduledSequence scheduledSequence = (ScheduledSequence) o;
        return Objects.equals(id, scheduledSequence.id) &&
                Objects.equals(sequence, scheduledSequence.sequence) &&
                Objects.equals(startTime, scheduledSequence.startTime) &&
                Objects.equals(endTime, scheduledSequence.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sequence, startTime, endTime);
    }
}
