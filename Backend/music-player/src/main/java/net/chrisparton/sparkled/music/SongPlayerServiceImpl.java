package net.chrisparton.sparkled.music;

import com.google.gson.Gson;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackListener;
import net.chrisparton.sparkled.entity.RenderedSong;
import net.chrisparton.sparkled.entity.Song;
import net.chrisparton.sparkled.entity.SongData;
import net.chrisparton.sparkled.persistence.song.SongPersistenceService;
import net.chrisparton.sparkled.renderdata.RenderedChannelMap;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;

public class SongPlayerServiceImpl implements SongPlayerService {

    private final SongPersistenceService songPersistenceService;
    private AudioDevice audioDevice;
    private PlaybackListener playbackListener;
    private Song currentSong;
    private RenderedChannelMap renderedChannelMap;

    @Inject
    public SongPlayerServiceImpl(SongPersistenceService songPersistenceService) {
        this.songPersistenceService = songPersistenceService;
    }

    @Override
    public void play(Song song, SongData songData) {
        this.currentSong = song;
        RenderedSong renderedSong = songPersistenceService.getRenderedSongById(song.getId()).orElse(new RenderedSong());
        this.renderedChannelMap = new Gson().fromJson(renderedSong.getRenderData(), RenderedChannelMap.class);

        try {
            FactoryRegistry r = FactoryRegistry.systemRegistry();
            audioDevice = r.createAudioDevice();

            ByteArrayInputStream bais = new ByteArrayInputStream(songData.getMp3Data());
            AdvancedPlayer player = new AdvancedPlayer(bais, audioDevice);
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

            return playbackMillis / songDurationMillis;
        }
    }

    @Override
    public void setPlaybackListener(PlaybackListener playbackListener) {
        this.playbackListener = playbackListener;
    }
}
