package net.chrisparton.sparkled.music;

import javazoom.jl.player.advanced.PlaybackListener;
import net.chrisparton.sparkled.entity.ScheduledSong;
import net.chrisparton.sparkled.entity.Song;
import net.chrisparton.sparkled.entity.SongData;
import net.chrisparton.sparkled.event.SongScheduledEvent;
import net.chrisparton.sparkled.persistence.scheduler.ScheduledSongPersistenceService;
import net.chrisparton.sparkled.persistence.song.SongPersistenceService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SongSchedulerServiceImpl implements SongSchedulerService {

    private static final Logger logger = Logger.getLogger(SongSchedulerServiceImpl.class.getName());

    private final SongPlayerService songPlayerService;
    private final ScheduledExecutorService executor;
    private final ScheduledSongPersistenceService scheduledSongPersistenceService;
    private final SongPersistenceService songPersistenceService;
    private ScheduledFuture<?> nextScheduledSong;

    @Inject
    public SongSchedulerServiceImpl(SongPlayerService songPlayerService,
                                    ScheduledSongPersistenceService scheduledSongPersistenceService,
                                    SongPersistenceService songPersistenceService) {
        this.songPlayerService = songPlayerService;
        this.scheduledSongPersistenceService = scheduledSongPersistenceService;
        this.songPersistenceService = songPersistenceService;
        this.executor = Executors.newSingleThreadScheduledExecutor();

        final PlaybackListener playbackListener = new SongPlaybackListener(this::scheduleNextSong);
        songPlayerService.setPlaybackListener(playbackListener);
    }

    @Override
    public void start() {
        EventBus.getDefault().register(this);
        scheduleNextSong();
    }

    @Subscribe
    public void onSongScheduled(SongScheduledEvent event) {
        logger.info("Received song scheduled event. Song scheduled: " + event.getScheduledSong().getSong().getName());
        scheduleNextSong();
    }

    private void scheduleNextSong() {
        Optional<ScheduledSong> nextSong = scheduledSongPersistenceService.getNextScheduledSong();
        nextSong.ifPresent(this::scheduleSong);
    }

    private void scheduleSong(ScheduledSong scheduledSong) {
        Date startTime = scheduledSong.getStartTime();
        LocalDateTime startDateTime = LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault());
        logger.info("Scheduling next song for " + startDateTime);

        long delay = ChronoUnit.MILLIS.between(LocalDateTime.now(), startDateTime);
        if (nextScheduledSong != null && !nextScheduledSong.isDone()) {
            logger.info("A song is already scheduled, cancelling it.");
            nextScheduledSong.cancel(true);
        }
        nextScheduledSong = executor.schedule(() -> playSong(scheduledSong.getSong()), delay, TimeUnit.MILLISECONDS);
    }

    private void playSong(Song song) {
        Optional<SongData> songData = songPersistenceService.getSongDataById(song.getId());
        songData.ifPresent(data -> songPlayerService.play(song, data));
    }
}
