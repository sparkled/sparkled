package net.chrisparton.xmas.entity;

import java.util.Arrays;
import java.util.List;

public enum AnimationEffectType {

    FLASH("Flash", AnimationEffectTypeParam.COLOUR),
    LINE_LEFT("Line Left", AnimationEffectTypeParam.MULTI_COLOUR),
    LINE_RIGHT("Line Right", AnimationEffectTypeParam.MULTI_COLOUR);

    private String name;
    private List<AnimationEffectTypeParam> parameters;

    AnimationEffectType(String name, AnimationEffectTypeParam... parameters) {
        this.name = name;
        this.parameters = Arrays.asList(parameters);
    }

    public String getName() {
        return name;
    }

    public List<AnimationEffectTypeParam> getParameters() {
        return parameters;
    }
}