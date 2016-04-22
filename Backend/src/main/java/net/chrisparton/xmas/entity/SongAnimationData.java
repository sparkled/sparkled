package net.chrisparton.xmas.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SongAnimationData {

    private List<AnimationEffectChannel> channels = new ArrayList<>();

    public List<AnimationEffectChannel> getChannels() {
        return channels;
    }

    public void setChannels(List<AnimationEffectChannel> channels) {
        this.channels = channels;
    }
}
