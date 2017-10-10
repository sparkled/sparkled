package net.chrisparton.sparkled.model.animation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnimationEffectParam {

    private AnimationEffectTypeParamCode paramCode;
    private String value;
    private List<String> multiValue = new ArrayList<>();

    public AnimationEffectParam() {
    }

    public AnimationEffectTypeParamCode getParamCode() {
        return paramCode;
    }

    public void setParamCode(AnimationEffectTypeParamCode paramCode) {
        this.paramCode = paramCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getMultiValue() {
        return multiValue;
    }

    public void setMultiValue(List<String> multiValue) {
        this.multiValue = multiValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof AnimationEffectParam)) {
            return false;
        }

        AnimationEffectParam that = (AnimationEffectParam) o;
        return Objects.equals(paramCode, that.paramCode) &&
                Objects.equals(value, that.value) &&
                Objects.equals(multiValue, that.multiValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paramCode, value, multiValue);
    }
}
