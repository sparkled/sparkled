package net.chrisparton.sparkled.entity;

import java.util.*;

public class AnimationEffectType {

    private String code;
    private String name;
    private List<AnimationEffectTypeParam> parameters;

    AnimationEffectType(String code, String name, AnimationEffectTypeParam... parameters) {
        this.code = code;
        this.name = name;
        this.parameters = Arrays.asList(parameters);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AnimationEffectTypeParam> getParameters() {
        return parameters;
    }

    public void setParameters(List<AnimationEffectTypeParam> parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof AnimationEffectType)) {
            return false;
        }

        AnimationEffectType that = (AnimationEffectType) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(name, that.name) &&
                Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, parameters);
    }
}