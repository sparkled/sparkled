package net.chrisparton.xmas.entity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AnimationEffectType {

    public static final AnimationEffectType FLASH = new AnimationEffectType("FLASH", "Flash", AnimationEffectTypeParam.COLOUR);
    public static final AnimationEffectType LINE_LEFT = new AnimationEffectType("LINE_LEFT", "Line Left", AnimationEffectTypeParam.MULTI_COLOUR);
    public static final AnimationEffectType LINE_RIGHT = new AnimationEffectType("LINE_RIGHT", "Line Right", AnimationEffectTypeParam.MULTI_COLOUR);
    public static final List<AnimationEffectType> TYPES = Arrays.asList(
            FLASH, LINE_LEFT, LINE_RIGHT
    );

    private String code;
    private String name;
    private List<AnimationEffectTypeParam> parameters;

    public AnimationEffectType(String code, String name, AnimationEffectTypeParam... parameters) {
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