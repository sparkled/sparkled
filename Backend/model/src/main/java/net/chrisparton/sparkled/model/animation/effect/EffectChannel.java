package net.chrisparton.sparkled.model.animation.effect;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EffectChannel {

    private List<Effect> effects = new ArrayList<>();
    private String name;
    private String code;
    private int startLed = 0;
    private int endLed = 0;
    private int displayOrder;

    public int getLedCount() {
        return endLed - startLed + 1;
    }

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

    public String getCode() {
        return code;
    }

    public EffectChannel setCode(String code) {
        this.code = code;
        return this;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public EffectChannel setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    public int getStartLed() {
        return startLed;
    }

    public EffectChannel setStartLed(int startLed) {
        this.startLed = startLed;
        return this;
    }

    public int getEndLed() {
        return endLed;
    }

    public EffectChannel setEndLed(int endLed) {
        this.endLed = endLed;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EffectChannel)) return false;
        EffectChannel that = (EffectChannel) o;
        return startLed == that.startLed &&
                endLed == that.endLed &&
                displayOrder == that.displayOrder &&
                Objects.equals(effects, that.effects) &&
                Objects.equals(name, that.name) &&
                Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(effects, name, code, startLed, endLed, displayOrder);
    }

    @Override
    public String toString() {
        return "EffectChannel{" +
                "effects=" + effects +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", startLed=" + startLed +
                ", endLed=" + endLed +
                ", displayOrder=" + displayOrder +
                '}';
    }
}
