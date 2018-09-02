package io.sparkled.model.animation;

import io.sparkled.model.animation.effect.EffectChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SequenceAnimationData {

    private List<EffectChannel> channels = new ArrayList<>();

    public List<EffectChannel> getChannels() {
        return channels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SequenceAnimationData)) return false;
        SequenceAnimationData that = (SequenceAnimationData) o;
        return Objects.equals(channels, that.channels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channels);
    }

    @Override
    public String toString() {
        return "SequenceAnimationData{" +
                "channels=" + channels +
                '}';
    }
}
