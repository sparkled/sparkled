package net.chrisparton.sparkled.entity;

import net.chrisparton.sparkled.renderer.effect.*;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public enum AnimationEffectTypeCode {

    FILL(
            new AnimationEffectType(
                    "FILL",
                    "Fill",
                    AnimationEffectTypeParamCode.COLOUR.getEffectTypeParam()
            ),
            new FillEffectRenderer()
    ),
    FLASH(
            new AnimationEffectType(
                    "FLASH",
                    "Flash",
                    AnimationEffectTypeParamCode.COLOUR.getEffectTypeParam()
            ),
            new FlashEffectRenderer()
    ),
    LINE_LEFT(
            new AnimationEffectType(
                    "LINE_LEFT",
                    "Line Left",
                    AnimationEffectTypeParamCode.MULTI_COLOUR.getEffectTypeParam(),
                    AnimationEffectTypeParamCode.LENGTH.getEffectTypeParam()
            ),
            new LineLeftEffectRenderer()
    ),
    LINE_RIGHT(
            new AnimationEffectType(
                    "LINE_RIGHT",
                    "Line Right",
                    AnimationEffectTypeParamCode.MULTI_COLOUR.getEffectTypeParam(),
                    AnimationEffectTypeParamCode.LENGTH.getEffectTypeParam()
            ),
            new LineRightEffectRenderer()
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