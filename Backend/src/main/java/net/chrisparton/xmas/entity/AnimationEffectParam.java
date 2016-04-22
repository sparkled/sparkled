package net.chrisparton.xmas.entity;

import java.util.Objects;

public class AnimationEffectParam {

    private AnimationEffectTypeParamCode paramCode;
    private String value;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof AnimationEffectParam)) {
            return false;
        }

        AnimationEffectParam that = (AnimationEffectParam) o;
        return Objects.equals(paramCode, that.paramCode) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paramCode, value);
    }
}
