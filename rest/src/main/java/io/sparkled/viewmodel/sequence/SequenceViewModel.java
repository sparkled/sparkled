package io.sparkled.viewmodel.sequence;

import io.sparkled.viewmodel.sequence.channel.SequenceChannelViewModel;

import java.util.List;

public class SequenceViewModel {
    private Integer id;
    private String name;
    private int durationFrames;
    private int framesPerSecond = 60;
    private List<SequenceChannelViewModel> channels;

    public Integer getId() {
        return id;
    }

    public SequenceViewModel setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public SequenceViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public int getDurationFrames() {
        return durationFrames;
    }

    public SequenceViewModel setDurationFrames(int durationFrames) {
        this.durationFrames = durationFrames;
        return this;
    }

    public int getFramesPerSecond() {
        return framesPerSecond;
    }

    public SequenceViewModel setFramesPerSecond(int framesPerSecond) {
        this.framesPerSecond = framesPerSecond;
        return this;
    }

    public List<SequenceChannelViewModel> getChannels() {
        return channels;
    }

    public SequenceViewModel setChannels(List<SequenceChannelViewModel> channels) {
        this.channels = channels;
        return this;
    }
}
