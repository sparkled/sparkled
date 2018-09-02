package io.sparkled.music;

import io.sparkled.model.entity.ScheduledSequence;

/**
 * Uses {@link ScheduledSequence} records to schedule and play sequences at appropriate times.
 */
public interface SequenceSchedulerService {

    /**
     * Start the service and load the next scheduled sequence.
     */
    void start();

    /**
     * Load the next scheduled sequence.
     */
    void scheduleNextSequence();
}
