package net.chrisparton.xmas.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SongAnimationData {

    private List<AnimationEffectChannel> channels = new ArrayList<>();

    public SongAnimationData() {
        channels.addAll(Arrays.asList(
                new AnimationEffectChannel("Pillar 1", 0, 200, 249),
                new AnimationEffectChannel("Pillar 2", 1, 250, 299),
                new AnimationEffectChannel("Pillar 3", 2, 249, 300),
                new AnimationEffectChannel("Pillar 4", 3, 350, 399),
                new AnimationEffectChannel("Arch 1", 4, 0, 49),
                new AnimationEffectChannel("Arch 2", 5, 50, 99),
                new AnimationEffectChannel("Arch 3", 6, 100, 149),
                new AnimationEffectChannel("Arch 4", 7, 150, 299),
                new AnimationEffectChannel("Global", 8, 0, 399)
        ));
    }

    public List<AnimationEffectChannel> getChannels() {
        return channels;
    }

    public void setChannels(List<AnimationEffectChannel> channels) {
        this.channels = channels;
    }
}
