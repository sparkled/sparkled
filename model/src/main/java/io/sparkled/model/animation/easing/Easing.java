package io.sparkled.model.animation.easing;

import io.sparkled.model.animation.param.Param;
import io.sparkled.model.animation.param.HasParams;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Easing implements HasParams {

    private EasingTypeCode type;
    private List<Param> params;

    public EasingTypeCode getType() {
        return type;
    }

    public Easing setType(EasingTypeCode type) {
        this.type = type;
        return this;
    }

    @Override
    public List<Param> getParams() {
        return params;
    }

    public Easing setParams(Param... params) {
        return setParams(Arrays.asList(params));
    }

    public Easing setParams(List<Param> params) {
        this.params = params;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Easing)) return false;
        Easing easing = (Easing) o;
        return type == easing.type &&
                Objects.equals(params, easing.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, params);
    }

    @Override
    public String toString() {
        return "Easing{" +
                "type=" + type +
                ", params=" + params +
                '}';
    }
}