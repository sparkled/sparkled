package io.sparkled.model.animation;

import io.sparkled.model.animation.effect.Effect;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SequenceChannelEffects {

    private List<Effect> effects = new ArrayList<>();

    public List<Effect> getEffects() {
        return effects;
    }

    public SequenceChannelEffects setEffects(List<Effect> effects) {
        this.effects = effects;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SequenceChannelEffects)) return false;
        SequenceChannelEffects that = (SequenceChannelEffects) o;
        return Objects.equals(effects, that.effects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(effects);
    }

    @Override
    public String toString() {
        return "SequenceChannelEffects{" +
                "effects=" + effects +
                '}';
    }
}
