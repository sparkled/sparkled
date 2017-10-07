package net.chrisparton.sparkled.entity;

import net.chrisparton.sparkled.renderer.effect.*;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public enum AnimationEffectTypeCode {

    FLASH(
            new AnimationEffectType(
                    "FLASH",
                    "Flash",
                    AnimationEffectTypeParamCode.COLOUR.getEffectTypeParam()
            ),
            new FlashEffectRenderer()
    ),
    LINE(
            new AnimationEffectType(
                    "LINE",
                    "Line",
                    AnimationEffectTypeParamCode.MULTI_COLOUR.getEffectTypeParam(),
                    AnimationEffectTypeParamCode.LENGTH.getEffectTypeParam()
            ),
            new LineEffectRenderer()
    );

    public static final List<AnimationEffectType> EFFECT_TYPES = Arrays
            .stream(AnimationEffectTypeCode.values())
            .map(AnimationEffectTypeCode::getEffectType)
            .collect(toList());

    private AnimationEffectType effectType;
    private EffectRenderer renderer;

    AnimationEffectTypeCode(AnimationEffectType effectType, EffectRenderer renderer) {
        this.effectType = effectType;
        this.renderer = renderer;
    }

    public AnimationEffectType getEffectType() {
        return effectType;
    }

    public EffectRenderer getRenderer() {
        return renderer;
    }
}