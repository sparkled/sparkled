package net.chrisparton.xmas.entity;

import java.util.Objects;

public class AnimationEffectParam {

    private String paramCode;
    private String paramName;
    private String value;

    public AnimationEffectParam(String paramCode, String paramName, String value) {
        this.paramCode = paramCode;
        this.paramName = paramName;
        this.value = value;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
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
                Objects.equals(paramName, that.paramName) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paramCode, paramName, value);
    }
}
