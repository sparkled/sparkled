package net.chrisparton.sparkled.entity;

import java.util.Objects;

public class AnimationEffectParamValue {

    private String value;

    public AnimationEffectParamValue() {
    }

    public AnimationEffectParamValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnimationEffectParamValue)) return false;
        AnimationEffectParamValue that = (AnimationEffectParamValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
