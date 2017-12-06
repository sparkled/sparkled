package net.chrisparton.sparkled.model.animation.effect;

import net.chrisparton.sparkled.model.animation.param.HasParams;
import net.chrisparton.sparkled.model.animation.param.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EffectType implements HasParams {

    private EffectTypeCode code;
    private String name;
    private List<Param> params = new ArrayList<>();

    public EffectTypeCode getCode() {
        return code;
    }

    public EffectType setCode(EffectTypeCode code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public EffectType setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public List<Param> getParams() {
        return params;
    }

    public EffectType setParams(List<Param> params) {
        this.params = params;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EffectType)) return false;
        EffectType that = (EffectType) o;
        return code == that.code &&
                Objects.equals(name, that.name) &&
                Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, params);
    }

    @Override
    public String toString() {
        return "EffectType{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", params=" + params +
                '}';
    }
}
