package io.sparkled.viewmodel.sequence;

import io.sparkled.model.entity.SequenceStatus;
import io.sparkled.viewmodel.sequence.channel.SequenceChannelViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SequenceViewModel {
    private Integer id;
    private Integer stageId;
    private String name;
    private String artist;
    private String album;
    private Integer durationFrames;
    private Integer framesPerSecond = 60;
    private SequenceStatus status;
    private List<SequenceChannelViewModel> channels = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public SequenceViewModel setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getStageId() {
        return stageId;
    }

    public SequenceViewModel setStageId(Integer stageId) {
        this.stageId = stageId;
        return this;
    }

    public String getName() {
        return name;
    }

    public SequenceViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getArtist() {
        return artist;
    }

    public SequenceViewModel setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getAlbum() {
        return album;
    }

    public SequenceViewModel setAlbum(String album) {
        this.album = album;
        return this;
    }

    public Integer getDurationFrames() {
        return durationFrames;
    }

    public SequenceViewModel setDurationFrames(Integer durationFrames) {
        this.durationFrames = durationFrames;
        return this;
    }

    public Integer getFramesPerSecond() {
        return framesPerSecond;
    }

    public SequenceViewModel setFramesPerSecond(Integer framesPerSecond) {
        this.framesPerSecond = framesPerSecond;
        return this;
    }

    public SequenceStatus getStatus() {
        return status;
    }

    public SequenceViewModel setStatus(SequenceStatus status) {
        this.status = status;
        return this;
    }

    public List<SequenceChannelViewModel> getChannels() {
        return channels;
    }

    public SequenceViewModel setChannels(List<SequenceChannelViewModel> channels) {
        this.channels = channels;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SequenceViewModel that = (SequenceViewModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(stageId, that.stageId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(artist, that.artist) &&
                Objects.equals(album, that.album) &&
                Objects.equals(durationFrames, that.durationFrames) &&
                Objects.equals(framesPerSecond, that.framesPerSecond) &&
                status == that.status &&
                Objects.equals(channels, that.channels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stageId, name, artist, album, durationFrames, framesPerSecond, status, channels);
    }

    @Override
    public String toString() {
        return "SequenceViewModel{" +
                "id=" + id +
                ", stageId=" + stageId +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", durationFrames=" + durationFrames +
                ", framesPerSecond=" + framesPerSecond +
                ", status=" + status +
                ", channels=" + channels +
                '}';
    }
}
