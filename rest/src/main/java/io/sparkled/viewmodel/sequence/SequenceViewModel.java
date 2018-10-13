package io.sparkled.viewmodel.sequence;

import io.sparkled.model.entity.SequenceStatus;
import io.sparkled.viewmodel.ViewModel;
import io.sparkled.viewmodel.sequence.channel.SequenceChannelViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SequenceViewModel implements ViewModel {

    private Integer id;
    private Integer songId;
    private Integer stageId;
    private String name;
    private Integer framesPerSecond;
    private Integer frameCount;
    private SequenceStatus status;
    private List<SequenceChannelViewModel> channels = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public SequenceViewModel setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getSongId() {
        return songId;
    }

    public SequenceViewModel setSongId(Integer songId) {
        this.songId = songId;
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

    public Integer getFramesPerSecond() {
        return framesPerSecond;
    }

    public SequenceViewModel setFramesPerSecond(Integer framesPerSecond) {
        this.framesPerSecond = framesPerSecond;
        return this;
    }

    public Integer getFrameCount() {
        return frameCount;
    }

    public SequenceViewModel setFrameCount(Integer frameCount) {
        this.frameCount = frameCount;
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
                Objects.equals(songId, that.songId) &&
                Objects.equals(stageId, that.stageId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(framesPerSecond, that.framesPerSecond) &&
                Objects.equals(frameCount, that.frameCount) &&
                status == that.status &&
                Objects.equals(channels, that.channels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, songId, stageId, name, framesPerSecond, frameCount, status, channels);
    }

    @Override
    public String toString() {
        return "SongViewModel{" +
                "id=" + id +
                ", songId=" + songId +
                ", stageId=" + stageId +
                ", name='" + name + '\'' +
                ", framesPerSecond=" + framesPerSecond +
                ", frameCount=" + frameCount +
                ", status=" + status +
                ", channels=" + channels +
                '}';
    }
}
