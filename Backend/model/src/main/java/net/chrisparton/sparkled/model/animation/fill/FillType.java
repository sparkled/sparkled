package net.chrisparton.sparkled.model.animation.fill;

import net.chrisparton.sparkled.model.animation.param.HasParams;
import net.chrisparton.sparkled.model.animation.param.Param;

import java.util.List;
import java.util.Objects;

public class FillType implements HasParams {

    private FillTypeCode code;
    private String name;
    private List<Param> params;

    public FillTypeCode getCode() {
        return code;
    }

    public FillType setCode(FillTypeCode code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public FillType setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public List<Param> getParams() {
        return params;
    }

    public FillType setParams(List<Param> params) {
        this.params = params;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FillType)) return false;
        FillType fillType = (FillType) o;
        return code == fillType.code &&
                Objects.equals(name, fillType.name) &&
                Objects.equals(params, fillType.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, params);
    }

    @Override
    public String toString() {
        return "FillType{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", params=" + params +
                '}';
    }
}