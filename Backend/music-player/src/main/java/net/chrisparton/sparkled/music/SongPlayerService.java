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

import java.io.ByteArrayInputStream;

public class SongPlayerService {

    private AudioDevice audioDevice;
    private PlaybackListener playbackListener;
    private final SongPersistenceService persistenceService = new SongPersistenceService();
    private Song currentSong;
    private RenderedChannelMap renderedChannelMap;

    public SongPlayerService(PlaybackListener playbackListener) {
        assert playbackListener != null;
        this.playbackListener = playbackListener;
    }

    public void play(Song song, SongData songData) {
        this.currentSong = song;
        RenderedSong renderedSong = persistenceService.getRenderedSongById(song.getId()).orElse(new RenderedSong());
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

    /**
     * @return The song that is currently playing, or null if no song is playing.
     */
    public Song getCurrentSong() {
        return currentSong;
    }

    /**
     * @return The rendered data for the song that is currently playing, or null if no song is playing.
     */
    public RenderedChannelMap getRenderedChannelMap() {
        return renderedChannelMap;
    }

    /**
     * @return A value between 0 and 1 indicating the playback progress of the current song.
     */
    public double getSongProgress() {
        if (audioDevice == null || currentSong == null) {
            return 0d;
        } else {
            int playbackMillis = audioDevice.getPosition();
            double songDurationMillis = currentSong.getDurationFrames() / (double) currentSong.getFramesPerSecond() * 1000;

            return playbackMillis / songDurationMillis;
        }
    }
}
