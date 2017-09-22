package net.chrisparton.sparkled.music;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackListener;
import net.chrisparton.sparkled.entity.Song;
import net.chrisparton.sparkled.entity.SongData;

import java.io.ByteArrayInputStream;

public class SongPlayerService {

    private AudioDevice audioDevice;
    private PlaybackListener playbackListener;
    private Song currentSong;

    public SongPlayerService(PlaybackListener playbackListener) {
        assert playbackListener != null;
        this.playbackListener = playbackListener;
    }

    public void play(Song song, SongData songData) {
        this.currentSong = song;

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
