package net.chrisparton.sparkled.music;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.chrisparton.sparkled.model.entity.ScheduledSong;
import net.chrisparton.sparkled.model.entity.Song;
import net.chrisparton.sparkled.model.entity.SongAudio;
import net.chrisparton.sparkled.persistence.scheduler.ScheduledSongPersistenceService;
import net.chrisparton.sparkled.persistence.song.SongPersistenceService;

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
public class SongSchedulerServiceImpl implements SongSchedulerService {

    private static final Logger logger = Logger.getLogger(SongSchedulerServiceImpl.class.getName());

    private final SongPlayerService songPlayerService;
    private final ScheduledExecutorService executor;
    private final ScheduledSongPersistenceService scheduledSongPersistenceService;
    private final SongPersistenceService songPersistenceService;
    private final AtomicInteger lastAutoScheduledSongId = new AtomicInteger(0);
    private ScheduledFuture<?> nextScheduledSong;

    @Inject
    public SongSchedulerServiceImpl(SongPlayerService songPlayerService,
                                    ScheduledSongPersistenceService scheduledSongPersistenceService,
                                    SongPersistenceService songPersistenceService) {
        this.songPlayerService = songPlayerService;
        this.scheduledSongPersistenceService = scheduledSongPersistenceService;
        this.songPersistenceService = songPersistenceService;

        this.executor = Executors.newSingleThreadScheduledExecutor(
                new ThreadFactoryBuilder().setNameFormat("song-scheduler-%d").build()
        );

        songPlayerService.addPlaybackFinishedListener(event -> this.scheduleNextSong());
    }

    @Override
    public void start() {
        scheduleNextSong();
    }

    @Override
    public void scheduleNextSong() {
        Optional<ScheduledSong> nextSong = scheduledSongPersistenceService.getNextScheduledSong();
        ScheduledSong scheduledSong = nextSong.orElse(null);
        if (scheduledSong != null) {
            scheduleSong(scheduledSong);
        }

        if (scheduledSong == null || millisBetween(scheduledSong.getStartTime(), LocalDateTime.now()) > ScheduledSong.MIN_SECONDS_BETWEEN_SONGS * 2) {
            Optional<Song> nextAutoSchedulableSong = scheduledSongPersistenceService.getNextAutoSchedulableSong(lastAutoScheduledSongId.get());
            if (nextAutoSchedulableSong.isPresent()) {
                logger.info("Playing auto schedulable song until next scheduled song is due to be played.");
                Song song = nextAutoSchedulableSong.get();
                lastAutoScheduledSongId.set(song.getId());
                executor.schedule(() -> playSong(song), 0, TimeUnit.MILLISECONDS);
            }
        }
    }

    private void scheduleSong(ScheduledSong scheduledSong) {
        long delay = millisBetween(scheduledSong.getStartTime(), LocalDateTime.now());
        logger.info("Playing next song in " + delay + "ms.");

        if (nextScheduledSong != null && !nextScheduledSong.isDone()) {
            logger.info("A song is already scheduled, cancelling it.");
            nextScheduledSong.cancel(true);
        }
        nextScheduledSong = executor.schedule(() -> playSong(scheduledSong.getSong()), delay, TimeUnit.MILLISECONDS);
    }

    private void playSong(Song song) {
        Optional<SongAudio> songData = songPersistenceService.getSongDataById(song.getId());
        songData.ifPresent(data -> songPlayerService.play(song, data));
    }

    private long millisBetween(Date lhs, LocalDateTime rhs) {
        LocalDateTime startDateTime = LocalDateTime.ofInstant(lhs.toInstant(), ZoneId.systemDefault());
        return ChronoUnit.MILLIS.between(startDateTime, rhs);
    }
}
