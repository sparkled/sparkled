package io.sparkled.persistence.scheduler.impl.query;

import io.sparkled.model.entity.ScheduledSequence;
import io.sparkled.model.entity.Sequence;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.sequence.impl.query.GetSequenceByIdQuery;
import org.apache.commons.lang3.time.DateUtils;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.Optional;

public class SaveScheduledSequenceQuery implements PersistenceQuery<Boolean> {

    private final ScheduledSequence scheduledSequence;

    public SaveScheduledSequenceQuery(ScheduledSequence scheduledSequence) {
        this.scheduledSequence = scheduledSequence;
    }

    @Override
    public Boolean perform(EntityManager entityManager) {
        Sequence providedSequence = this.scheduledSequence.getSequence();
        if (providedSequence == null) {
            return false;
        }

        Optional<Sequence> sequence = new GetSequenceByIdQuery(providedSequence.getId()).perform(entityManager);
        if (!sequence.isPresent()) {
            return false;
        }

        ScheduledSequence scheduledSequence = this.scheduledSequence;
        scheduledSequence.setEndTime(calculateEndTime(scheduledSequence));
        scheduledSequence = entityManager.merge(scheduledSequence);
        return scheduledSequence != null;
    }

    private Date calculateEndTime(ScheduledSequence scheduledSequence) {
        Sequence sequence = scheduledSequence.getSequence();
        Date endTime = null;

        if (sequence != null) {
            Date startTime = scheduledSequence.getStartTime();
            int durationSeconds = (int) Math.ceil(sequence.getDurationFrames() / (double) sequence.getFramesPerSecond());
            endTime = DateUtils.addSeconds(startTime, durationSeconds);
        }

        return endTime;
    }
}
