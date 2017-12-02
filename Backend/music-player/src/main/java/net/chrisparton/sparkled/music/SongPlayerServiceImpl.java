package net.chrisparton.sparkled.music;

import com.google.gson.Gson;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import net.chrisparton.sparkled.model.entity.RenderedSong;
import net.chrisparton.sparkled.model.entity.Song;
import net.chrisparton.sparkled.model.entity.SongAudio;
import net.chrisparton.sparkled.persistence.song.SongPersistenceService;
import net.chrisparton.sparkled.model.render.RenderedChannelMap;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.ByteArrayInputStream;
import java.util.function.Consumer;
import java.util.logging.Logger;

@Singleton
public class SongPlayerServiceImpl implements SongPlayerService {

    private final Logger logger = Logger.getLogger(SongPlayerServiceImpl.class.getName());
    private final SongPersistenceService songPersistenceService;
    private final AggregatePlaybackListener playbackListener = new AggregatePlaybackListener();
    private AudioDevice audioDevice;
    private Song currentSong;
    private RenderedChannelMap renderedChannelMap;

    @Inject
    public SongPlayerServiceImpl(SongPersistenceService songPersistenceService) {
        this.songPersistenceService = songPersistenceService;
        this.playbackListener.addPlaybackFinishedListener(event -> {
            logger.info("Song playback finished.");
            this.stopCurrentSong();
        });
    }

    @Override
    public void play(Song song, SongAudio songAudio) {
        this.currentSong = song;
        RenderedSong renderedSong = songPersistenceService.getRenderedSongById(song.getId()).orElse(new RenderedSong());
        this.renderedChannelMap = new Gson().fromJson(renderedSong.getRenderData(), RenderedChannelMap.class);

        try {
            FactoryRegistry r = FactoryRegistry.systemRegistry();
            audioDevice = r.createAudioDevice();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(songAudio.getAudioData());
            AdvancedPlayer player = new AdvancedPlayer(inputStream, audioDevice);
            player.setPlayBackListener(playbackListener);
            player.play();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Song getCurrentSong() {
        return currentSong;
    }

    @Override
    public RenderedChannelMap getRenderedChannelMap() {
        return renderedChannelMap;
    }

    @Override
    public double getSongProgress() {
        if (audioDevice == null || currentSong == null) {
            return 0d;
        } else {
            int playbackMillis = audioDevice.getPosition();
            double songDurationMillis = currentSong.getDurationFrames() / (double) currentSong.getFramesPerSecond() * 1000;
            return Math.min(1f, playbackMillis / songDurationMillis);
        }
    }

    @Override
    public void addPlaybackFinishedListener(Consumer<PlaybackEvent> playbackListener) {
        this.playbackListener.addPlaybackFinishedListener(playbackListener);
    }

    @Override
    public void stopCurrentSong() {
        if (this.audioDevice != null && this.audioDevice.isOpen()) {
            this.audioDevice.close();
        }

        this.currentSong = null;
        this.renderedChannelMap = null;
        this.audioDevice = null;
    }
}
