package io.sparkled.persistence.scheduler;

import io.sparkled.model.entity.ScheduledSequence;
import io.sparkled.model.entity.Sequence;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ScheduledSequencePersistenceService {

    Optional<ScheduledSequence> getNextScheduledSequence();

    Optional<Sequence> getNextAutoSchedulableSequence(int lastSequenceId);

    List<ScheduledSequence> getScheduledSequences(Date startDate, Date endDate);

    Optional<ScheduledSequence> getScheduledSequenceAtTime(Date time);

    boolean removeScheduledSequence(int scheduledSequenceId);

    boolean saveScheduledSequence(ScheduledSequence scheduledSequence);
}
