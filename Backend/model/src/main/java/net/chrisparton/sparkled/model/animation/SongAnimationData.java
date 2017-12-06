package net.chrisparton.sparkled.model.animation;

import net.chrisparton.sparkled.model.animation.effect.EffectChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SongAnimationData {

    private List<EffectChannel> channels = new ArrayList<>();

    public List<EffectChannel> getChannels() {
        return channels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SongAnimationData)) return false;
        SongAnimationData that = (SongAnimationData) o;
        return Objects.equals(channels, that.channels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channels);
    }

    @Override
    public String toString() {
        return "SongAnimationData{" +
                "channels=" + channels +
                '}';
    }
}
