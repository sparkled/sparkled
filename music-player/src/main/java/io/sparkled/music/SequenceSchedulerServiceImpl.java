package io.sparkled.music;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.sparkled.model.entity.ScheduledSequence;
import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.SongAudio;
import io.sparkled.persistence.scheduler.ScheduledSequencePersistenceService;
import io.sparkled.persistence.sequence.SequencePersistenceService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@Singleton
public class SequenceSchedulerServiceImpl implements SequenceSchedulerService {

    private static final Logger logger = Logger.getLogger(SequenceSchedulerServiceImpl.class.getName());

    private final SequencePlayerService sequencePlayerService;
    private final ScheduledExecutorService executor;
    private final ScheduledSequencePersistenceService scheduledSequencePersistenceService;
    private final SequencePersistenceService sequencePersistenceService;
    private final AtomicInteger lastAutoScheduledSequenceId = new AtomicInteger(0);
    private ScheduledFuture<?> nextScheduledSequence;

    @Inject
    public SequenceSchedulerServiceImpl(SequencePlayerService sequencePlayerService,
                                        ScheduledSequencePersistenceService scheduledSequencePersistenceService,
                                        SequencePersistenceService sequencePersistenceService) {
        this.sequencePlayerService = sequencePlayerService;
        this.scheduledSequencePersistenceService = scheduledSequencePersistenceService;
        this.sequencePersistenceService = sequencePersistenceService;

        this.executor = Executors.newSingleThreadScheduledExecutor(
                new ThreadFactoryBuilder().setNameFormat("sequence-scheduler-%d").build()
        );

        sequencePlayerService.addPlaybackFinishedListener(event -> this.scheduleNextSequence());
    }

    @Override
    public void start() {
        scheduleNextSequence();
    }

    @Override
    public void scheduleNextSequence() {
        Optional<ScheduledSequence> nextSequence = scheduledSequencePersistenceService.getNextScheduledSequence();
        ScheduledSequence scheduledSequence = nextSequence.orElse(null);
        if (scheduledSequence != null) {
            scheduleSequence(scheduledSequence);
        }

        if (scheduledSequence == null || millisUntil(scheduledSequence.getStartTime()) > ScheduledSequence.MIN_SECONDS_BETWEEN_SEQUENCES * 1000) {
            Optional<Sequence> nextAutoSchedulableSequence = scheduledSequencePersistenceService.getNextAutoSchedulableSequence(lastAutoScheduledSequenceId.get());
            if (nextAutoSchedulableSequence.isPresent()) {
                logger.info("Playing auto schedulable sequence until next scheduled sequence is due to be played.");
                Sequence sequence = nextAutoSchedulableSequence.get();
                lastAutoScheduledSequenceId.set(sequence.getId());
                executor.schedule(() -> playSequence(sequence), 0, TimeUnit.MILLISECONDS);
            } else {
                logger.info("No auto schedulable sequences found. Checking again in " + ScheduledSequence.MIN_SECONDS_BETWEEN_SEQUENCES + " seconds.");
                executor.schedule(this::scheduleNextSequence, ScheduledSequence.MIN_SECONDS_BETWEEN_SEQUENCES, TimeUnit.SECONDS);
            }
        }
    }

    private void scheduleSequence(ScheduledSequence scheduledSequence) {
        long delay = millisUntil(scheduledSequence.getStartTime());
        logger.info("Playing next sequence in " + delay + "ms.");

        if (nextScheduledSequence != null && !nextScheduledSequence.isDone()) {
            logger.info("A sequence is already scheduled, cancelling it.");
            nextScheduledSequence.cancel(true);
        }
        nextScheduledSequence = executor.schedule(() -> playSequence(scheduledSequence.getSequence()), delay, TimeUnit.MILLISECONDS);
    }

    private void playSequence(Sequence sequence) {
        Optional<SongAudio> songAudio = sequencePersistenceService.getSongAudioBySequenceId(sequence.getId());
        songAudio.ifPresent(data -> sequencePlayerService.play(sequence, data));
    }

    private long millisUntil(Date lhs) {
        LocalDateTime startDateTime = LocalDateTime.ofInstant(lhs.toInstant(), ZoneId.systemDefault());
        return ChronoUnit.MILLIS.between(LocalDateTime.now(), startDateTime);
    }
}
