package io.sparkled.model.animation.effect;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EffectChannel {

    private List<Effect> effects = new ArrayList<>();
    private String name;
    private String propCode;
    private int displayOrder;

    public List<Effect> getEffects() {
        return effects;
    }

    public EffectChannel setEffects(List<Effect> effects) {
        this.effects = effects;
        return this;
    }

    public String getName() {
        return name;
    }

    public EffectChannel setName(String name) {
        this.name = name;
        return this;
    }

    public String getPropCode() {
        return propCode;
    }

    public EffectChannel setPropCode(String code) {
        this.propCode = code;
        return this;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public EffectChannel setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EffectChannel)) return false;
        EffectChannel that = (EffectChannel) o;
        return displayOrder == that.displayOrder &&
                Objects.equals(effects, that.effects) &&
                Objects.equals(name, that.name) &&
                Objects.equals(propCode, that.propCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(effects, name, propCode, displayOrder);
    }

    @Override
    public String toString() {
        return "EffectChannel{" +
                "effects=" + effects +
                ", name='" + name + '\'' +
                ", stagePropCode='" + propCode + '\'' +
                ", displayOrder=" + displayOrder +
                '}';
    }
}
