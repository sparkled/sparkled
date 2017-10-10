package net.chrisparton.sparkled.model.animation;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public enum AnimationEffectTypeCode {

    FLASH(
            new AnimationEffectType(
                    "FLASH",
                    "Flash",
                    AnimationEffectTypeParamCode.COLOUR.getEffectTypeParam()
            )
    ),
    LINE(
            new AnimationEffectType(
                    "LINE",
                    "Line",
                    AnimationEffectTypeParamCode.MULTI_COLOUR.getEffectTypeParam(),
                    AnimationEffectTypeParamCode.LENGTH.getEffectTypeParam()
            )
    );

    public static final List<AnimationEffectType> EFFECT_TYPES = Arrays
            .stream(AnimationEffectTypeCode.values())
            .map(AnimationEffectTypeCode::getEffectType)
            .collect(toList());

    private AnimationEffectType effectType;

    AnimationEffectTypeCode(AnimationEffectType effectType) {
        this.effectType = effectType;
    }

    public AnimationEffectType getEffectType() {
        return effectType;
    }
}