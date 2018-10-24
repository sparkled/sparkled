package io.sparkled.music;

import io.sparkled.model.entity.Playlist;
import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.Song;
import io.sparkled.model.entity.SongAudio;
import io.sparkled.model.render.RenderedStagePropDataMap;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * A container object holding all of the information pertaining to the current state of playback, in terms of audio
 * playback and associated rendered data for streaming to clients.
 */
public class PlaybackState {

    private final Playlist playlist;
    private final int playlistIndex;
    private final Supplier<Double> progressFunction;
    private final Sequence sequence;
    private final Song song;
    private final SongAudio songAudio;
    private final RenderedStagePropDataMap renderedStageProps;
    private final Map<String, UUID> stagePropUuids;

    public PlaybackState() {
        this(null, 0, null, null, null, null, null, null);
    }

    public PlaybackState(Playlist playlist, int playlistIndex, Supplier<Double> progressFunction, Sequence sequence, Song song, SongAudio songAudio, RenderedStagePropDataMap renderedStageProps, Map<String, UUID> stagePropUuids) {
        this.playlist = playlist;
        this.playlistIndex = playlistIndex;
        this.progressFunction = progressFunction;
        this.sequence = sequence;
        this.song = song;
        this.songAudio = songAudio;
        this.renderedStageProps = renderedStageProps;
        this.stagePropUuids = stagePropUuids;
    }

    public boolean isEmpty() {
        return playlist == null || sequence == null || song == null || songAudio == null || renderedStageProps == null || stagePropUuids == null;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public int getPlaylistIndex() {
        return playlistIndex;
    }

    public double getProgress() {
        return progressFunction.get();
    }

    public Sequence getSequence() {
        return sequence;
    }

    public Song getSong() {
        return song;
    }

    public SongAudio getSongAudio() {
        return songAudio;
    }

    public RenderedStagePropDataMap getRenderedStageProps() {
        return renderedStageProps;
    }

    public Map<String, UUID> getStagePropUuids() {
        return stagePropUuids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaybackState that = (PlaybackState) o;
        return playlistIndex == that.playlistIndex &&
                Objects.equals(playlist, that.playlist) &&
                Objects.equals(progressFunction, that.progressFunction) &&
                Objects.equals(sequence, that.sequence) &&
                Objects.equals(song, that.song) &&
                Objects.equals(songAudio, that.songAudio) &&
                Objects.equals(renderedStageProps, that.renderedStageProps) &&
                Objects.equals(stagePropUuids, that.stagePropUuids);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playlist, playlistIndex, progressFunction, sequence, song, songAudio, renderedStageProps, stagePropUuids);
    }

    @Override
    public String toString() {
        return "PlaybackState{" +
                "playlist=" + playlist +
                ", playlistIndex=" + playlistIndex +
                ", progressFunction=" + progressFunction +
                ", sequence=" + sequence +
                ", song=" + song +
                ", songAudio=" + songAudio +
                ", renderedStageProps=" + renderedStageProps +
                ", stagePropUuids=" + stagePropUuids +
                '}';
    }
}
