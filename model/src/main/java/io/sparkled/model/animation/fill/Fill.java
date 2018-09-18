package io.sparkled.model.animation.fill;

import io.sparkled.model.animation.param.HasParams;
import io.sparkled.model.animation.param.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Fill implements HasParams {

    private FillTypeCode type;
    private List<Param> params;

    public FillTypeCode getType() {
        return type;
    }

    public Fill setType(FillTypeCode type) {
        this.type = type;
        return this;
    }

    @Override
    public List<Param> getParams() {
        return params;
    }

    public Fill setParams(Param... params) {
        return setParams(Arrays.asList(params));
    }

    public Fill setParams(List<Param> params) {
        this.params = params;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fill)) return false;
        Fill fill = (Fill) o;
        return type == fill.type &&
                Objects.equals(params, fill.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, params);
    }

    @Override
    public String toString() {
        return "Fill{" +
                "type=" + type +
                ", params=" + params +
                '}';
    }
}