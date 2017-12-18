package io.sparkled.model.animation.easing;

import io.sparkled.model.animation.param.Param;
import io.sparkled.model.animation.param.HasParams;

import java.util.List;
import java.util.Objects;

public class EasingType implements HasParams {

    private EasingTypeCode code;
    private String name;
    private List<Param> params;

    public EasingTypeCode getCode() {
        return code;
    }

    public EasingType setCode(EasingTypeCode code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public EasingType setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public List<Param> getParams() {
        return params;
    }

    public EasingType setParams(List<Param> params) {
        this.params = params;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EasingType)) return false;
        EasingType easingType = (EasingType) o;
        return code == easingType.code &&
                Objects.equals(name, easingType.name) &&
                Objects.equals(params, easingType.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, params);
    }

    @Override
    public String toString() {
        return "EasingType{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", params=" + params +
                '}';
    }
}
