package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sequence_animation")
public class SequenceAnimation {

    @Id
    @Column(name = "sequence_id", nullable = false)
    private int sequenceId;

    @Basic
    @Column(name = "animation_data", columnDefinition = "text")
    private String animationData;

    public int getSequenceId() {
        return sequenceId;
    }

    public SequenceAnimation setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
        return this;
    }

    public String getAnimationData() {
        return animationData;
    }

    public SequenceAnimation setAnimationData(String animationData) {
        this.animationData = animationData;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SequenceAnimation sequenceAnimation = (SequenceAnimation) o;
        return sequenceId == sequenceAnimation.sequenceId &&
                Objects.equals(animationData, sequenceAnimation.animationData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequenceId, animationData);
    }
}
